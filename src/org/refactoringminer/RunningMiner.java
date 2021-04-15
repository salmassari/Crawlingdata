package org.refactoringminer;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.BufferedReader;
import java.io.File;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class RunningMiner {





        static Connection conn = null;
        static int IDCODE=9580564+1;

        //
//has an issues in LeetCode-Go, qt, cadence
        //https://github.com/kubernetes/autoscaler.git
        //  https://github.com/googleapis/google-api-go-client.git all data from same type.
        //https://github.com/go-qml/qml.git	qml has JDBC connection error.
        //https://github.com/koding/koding.git	koding has JDBC connection error.
        public static Connection connect() throws SQLException {

            String url = "jdbc:sqlite:/Users/salmassari/Downloads/Adding RefDiff.db";
            Connection conn = null;
            if(conn == null){
                conn = DriverManager.getConnection(url);

                //   } catch (SQLException e) {
                //     System.out.println("DB: error "+e.getMessage());
            }
            return conn;
        }

        public static void insert(String CommitID, String ProjectName, String RefactoringType, String PathSource, String PathTarget, String RefactoringDetails, String  ClassName, String ShortMeg, String FullMsg, String CommitterInfo,Connection conn) throws ClassNotFoundException, SQLException {
            if(conn==null){
                conn = connect();
                System.out.println("Conn gets null");
            }

            String sql = "INSERT INTO RefDiffGo(ID, CommitID, ProjectName,RefactoringType, PathSource, PathTarget, RefactoringDetails, ClassName, ShortMessage, FullMessage, CommitterInfo) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            Class.forName("org.sqlite.JDBC");
            try (
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, IDCODE++);
                pstmt.setString(2, CommitID);
                pstmt.setString(3, ProjectName);
                pstmt.setString(4, RefactoringType);
                pstmt.setString(5, PathSource);
                pstmt.setString(6, PathTarget);
                pstmt.setString(7, RefactoringDetails);
                pstmt.setString(8, ClassName);
                pstmt.setString(9, ShortMeg);
                pstmt.setString(10, FullMsg);
                pstmt.setString(11, CommitterInfo);

                pstmt.executeUpdate();
                pstmt.closeOnCompletion();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println(IDCODE);
            }
        }

        public static void main(String[] args) throws Exception {
            RunningMiner();
        }

        private static void RunningMiner() throws Exception {



                    GitService gitService = new GitServiceImpl();
                    GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();


                    Repository repo = gitService.cloneIfNotExists(
                           // "tmp/syncope",
                       //     "https://github.com/apache/syncope.git");
                            "tmp/refactoring-toy-example",
                            "https://github.com/danilofes/refactoring-toy-example.git");


                    String commitMessage = miner.getCommitMessage(repo, "master", new RefactoringHandler(){
                        @Override
                        public void handle(String commitId, List<Refactoring> refactorings){}
                    });

                    miner.detectAll(repo, "master", new RefactoringHandler() {
                        @Override
                        public void handle(String commitId, List<Refactoring> refactorings)  {
                            System.out.println("Refactorings at " + commitId);
                          //  System.out.println("Refactorings at " + new RevCommit(commitId).getFullMessage());
                            for (Refactoring ref : refactorings) {

//                                repo.readMergeCommitMsg();
                              System.out.println(ref.toString().intern());



                            }
                        }
                    });
//            System.out.println("This is the messages of the commit \t\t"+miner.getRefactorCommit().toString());
            ArrayList<String> refactorCommit =  miner.getRefactorCommit();


            for(String refCom: refactorCommit){
                System.out.println("Full message: "+refCom);
                System.out.print("DONE");
            }








        }








}
