package org.refactoringminer.rm1;

public class CommitDetails {


    public String FullMessage;
    public String ShortMessage;
    public String CommitID;
    public String Details;
    public int Time;


    public CommitDetails(String commitID, String shortMessage, String fullMessage, int time,String details) {
        FullMessage = fullMessage;
        ShortMessage = shortMessage;
        CommitID = commitID;
        Details = details;
        Time = time;
    }


    public String getFullMessage() {
        return FullMessage;
    }

    public void setFullMessage(String fullMessage) {
        FullMessage = fullMessage;
    }

    public String getShortMessage() {
        return ShortMessage;
    }

    public void setShortMessage(String shortMessage) {
        ShortMessage = shortMessage;
    }

    public String getCommitID() {
        return CommitID;
    }

    public void setCommitID(String commitID) {
        CommitID = commitID;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        this.Time = time;
    }



}
