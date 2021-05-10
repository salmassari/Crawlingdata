package org.refactoringminer;

public class RefactoringObject {

    public String RefactoringTyep;
    public String CommitID;
    public String PathSource;
    public String PathTarget;

    public RefactoringObject(String refactoringTyep, String commitID, String pathSource, String pathTarget) {
        RefactoringTyep = refactoringTyep;
        CommitID = commitID;
        PathSource = pathSource;
        PathTarget = pathTarget;
    }

    public String getRefactoringTyep() {
        return RefactoringTyep;
    }

    public void setRefactoringTyep(String refactoringTyep) {
        RefactoringTyep = refactoringTyep;
    }

    public String getCommitID() {
        return CommitID;
    }

    public void setCommitID(String commitID) {
        CommitID = commitID;
    }

    public String getPathSource() {
        return PathSource;
    }

    public void setPathSource(String pathSource) {
        PathSource = pathSource;
    }

    public String getPathTarget() {
        return PathTarget;
    }

    public void setPathTarget(String pathTarget) {
        PathTarget = pathTarget;
    }





    }

