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
public class RunningMinerCSV {

    static Connection conn = null;
    static int IDCODE=64596+1;
    static GitService gitService = new GitServiceImpl();
    static GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
    static ArrayList<CommitDetails> refactorCommit =  ((GitHistoryRefactoringMinerImpl) miner).getCommit();
    static ArrayList<RefactoringObject> refactors = new ArrayList<RefactoringObject>();


    public static void main(String[] args) throws Exception {
        RunningMiner();
    }

    private static void RunningMiner() throws Exception {
        String line="";
        BufferedReader br =new BufferedReader(new FileReader("data/Projects.csv"));
        line = br.readLine();

        while(line!=null) {

            try {
                int count= 0;
                Connection conn = connect();
                String[] ProName= line.split("	");
                Repository repo = gitService.cloneIfNotExists("tmp/"+ProName[1],ProName[0]);

                miner.detectAll(repo, "master", new RefactoringHandler() {
                    @Override
                    public void handle(String commitId, List<Refactoring> refactorings) {
                        System.out.println("Refactorings at " + commitId);
                        System.out.println("Refactorings at " + refactorings);
                        for (Refactoring ref : refactorings) {
                            try {
//                                System.out.println(ref.leftSide());
//                                System.out.println(ref.rightSide());

                                refactors.add(new RefactoringObject(ref.getName(), commitId, ref.leftSide().get(0).getFilePath(), ref.rightSide().get(0).getFilePath()));
                            }
                            catch(Exception e){
                                System.err.println(e.getStackTrace());
                                String left = "Empty";
                                String right = "Empty";
                                if(ref.leftSide().size() > 0){
                                    System.out.println("the left is exist");
                                    left = ref.leftSide().get(0).getFilePath();
                                }
                                if(ref.rightSide().size() > 0){
                                    System.out.println("the right is exist");
                                    right = ref.rightSide().get(0).getFilePath();

                                }
                                refactors.add(new RefactoringObject(ref.getName(), commitId, left, right));

                            }
                        }
                    }
                });


                System.out.println("DONE FROM Refactoring");
                for(CommitDetails refCom: refactorCommit){

                    try{
                        for (int counter = count; counter < refactors.size(); counter++) {
                            System.out.println(counter);
                            if (refCom.getCommitID().equals(refactors.get(counter).getCommitID())){
                                System.out.println("Commit ID: "+refCom.getCommitID());
                                insert(refactors.get(counter).getCommitID(),ProName[1],refactors.get(counter).getRefactoringTyep(), refCom.getFullMessage(),
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

    }

    public static Connection connect() throws SQLException {

        String url = "jdbc:sqlite:data/FR_database.db";
        Connection conn = null;
        if(conn == null){
            conn = DriverManager.getConnection(url);

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
