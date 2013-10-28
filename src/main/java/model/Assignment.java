
package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Assignment Model.
 * A data structure that records a assignment.
 * @author Haojie Huang
 */
public class Assignment {
    private String assID;
    private int assNo;
    private String assName;
    private Date deadline;
    private String specification;
    private ArrayList<Submission> submitRecord;

    public Assignment() {
		this.assID = "";
		this.assNo = -1;
		this.assName = "";
		this.deadline = null;
		this.specification = "";
        this.submitRecord = new ArrayList<Submission>();
    }

    public Assignment(String assID, int assNo, String assName, Date deadline, String specification, ArrayList<Submission> submitRecord) {
        this.assID = assID;
        this.assNo = assNo;
        this.assName = assName;
        this.deadline = deadline;
        this.specification = specification;
        this.submitRecord = submitRecord;
    }

    public String getAssID() {
		return assID;
	}

	public void setAssID(String assID) {
		this.assID = assID;
	}

	public int getAssNo() {
		return assNo;
	}

	public void setAssNo(int assNo) {
		this.assNo = assNo;
	}

	public String getAssName() {
        return assName;
    }

    public void setAssName(String assName) {
        this.assName = assName;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public ArrayList<Submission> getSubmitRecord() {
        return submitRecord;
    }

    public void setSubmitRecord(ArrayList<Submission> submitRecord) {
        this.submitRecord = submitRecord;
    }

	@Override
	public String toString() {
		return "Assignment{" + "assID=" + assID + ", assNo=" + assNo + ", assName=" + assName + ", deadline=" + deadline + ", specification=" + specification + ", submitRecord=" + submitRecord + '}';
	}
}
