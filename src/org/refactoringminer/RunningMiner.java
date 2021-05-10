package org.refactoringminer;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.CommitDetails;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
public class RunningMiner {

        static Connection conn = null;
        static int IDCODE=27583+1;


        public static void main(String[] args) throws Exception {
            RunningMiner();
        }

        private static void RunningMiner() throws Exception {
            GitService gitService = new GitServiceImpl();
            GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
            String line="";
            BufferedReader br =new BufferedReader(new FileReader("data/Projects.csv"));
            line = br.readLine();

            while(line!=null) {

                try {
                    ArrayList<CommitDetails> refactorCommit =  ((GitHistoryRefactoringMinerImpl) miner).getCommit();
                    ArrayList<RefactoringObject> refactors = new ArrayList<>();
                    int count= 0;
                    Connection conn = connect();
                    String[] gits= line.split("	");
                    String ProName = gits[1];
                    String folderpath="tmp/"+ProName;

                    Repository repo = gitService.cloneIfNotExists(
                            folderpath,
                            gits[0]);
//                    String commitMessage = miner.getCommitMessage(repo, "master", new RefactoringHandler(){
//                        @Override
//                        public void handle(String commitId, List<Refactoring> refactorings){}
//                    });

                    miner.detectAll(repo, "master", new RefactoringHandler() {
                        @Override
                        public void handle(String commitId, List<Refactoring> refactorings)  {
//                            System.out.println("Refactorings at " + commitId);
                            //  System.out.println("Refactorings at " + new RevCommit(commitId).getFullMessage());
                            for (Refactoring ref : refactorings) {
//                                System.out.println(commitId);
                                refactors.add(new RefactoringObject(ref.getName(),commitId,ref.leftSide().get(0).getFilePath(),ref.rightSide().get(0).getFilePath()));

                            }
                        }
                    });
                    for(CommitDetails refCom: refactorCommit){
//                        System.out.println(refCom);

                        try{
                            for (int counter = count; counter < refactors.size(); counter++) {
                                System.out.println(counter);

                                if (refCom.getCommitID().equals(refactors.get(counter).getCommitID())){
                                      System.out.println("Refactoring type: "+refactors.get(counter).getRefactoringTyep());
    //                                System.out.println("Ref ID: "+refactors.get(counter).getCommitID());
                                      System.out.println("Commit ID: "+refCom.getCommitID());
    //                                System.out.println("Full message: "+refCom.getFullMessage());
    //                                System.out.println("DONE "+ count);
                                    insert(refactors.get(counter).getCommitID(),ProName,refactors.get(counter).getRefactoringTyep(), refCom.getFullMessage(),
                                            refCom.getShortMessage(),refactors.get(counter).getPathSource(),refactors.get(counter).getPathTarget(),
                                            refCom.getDetails(),connect());

                                }
                                else{ System.out.println("BREAK "+ count);count= counter;break; }
                            }
                        } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(IDCODE);
                    }

                    }
                    conn.close();
                    System.out.println("DONE!!!");
                    line = br.readLine();
                    System.out.println(IDCODE);


                }catch( Exception e) {
                    e.printStackTrace();
                    System.out.println("caught an error");
                    line = br.readLine();
                    System.out.println(IDCODE);
                }
            }

//            ArrayList<String> refactorCommit =  miner.getRefactorCommit();


        }

    public static Connection connect() throws SQLException {

        String url = "jdbc:sqlite:data/FR_database.db";
        Connection conn = null;
        if(conn == null){
            conn = DriverManager.getConnection(url);

            //   } catch (SQLException e) {
            //     System.out.println("DB: error "+e.getMessage());
        }
        return conn;
    }

    public static void insert(String CommitID, String ProjectName, String RefactoringType, String ShortMeg, String FullMsg, String PathSource, String PathTarget,  String CommitterInfo,Connection conn) throws ClassNotFoundException, SQLException {
        if(conn==null){
            conn = connect();
            System.out.println("Conn gets null");
        }

        String sql = "INSERT INTO FeatureRequest(ID, ProjectName, CommitID, RefactoringType, ShortMessage, FullMessage, PathSource, PathTarget,CommitterInfo) VALUES(?,?,?,?,?,?,?,?,?)";
        Class.forName("org.sqlite.JDBC");
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, IDCODE++);
            pstmt.setString(2, ProjectName);
            pstmt.setString(3, CommitID);
            pstmt.setString(4, RefactoringType);
            pstmt.setString(5, ShortMeg);
            pstmt.setString(6, FullMsg);
            pstmt.setString(7, PathSource);
            pstmt.setString(8, PathTarget);
            pstmt.setString(9, CommitterInfo);

            pstmt.executeUpdate();
            pstmt.closeOnCompletion();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(IDCODE);
        }
    }








}
