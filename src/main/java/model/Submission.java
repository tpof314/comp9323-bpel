
package model;

import java.util.Date;

/**
 *
 * @author Haojie Huang
 */
public class Submission {
    private String stuName;
    private String projID;
    private Date submitTime;
    private double mark;

    public Submission() {
		this.stuName = "";
		this.projID = "";
		this.submitTime = null;
    	this.mark = -1;
    }
    
    public Submission(String stuName, String projID, Date submitTime, double mark) {
        this.stuName = stuName;
        this.projID = projID;
        this.submitTime = submitTime;
        this.mark = mark;
    }
    
    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public String getProjID() {
        return projID;
    }

    public void setProjID(String projID) {
        this.projID = projID;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

	@Override
	public String toString() {
		return "Submission{" + "stuName=" + stuName + ", projID=" + projID + ", submitTime=" + submitTime + ", mark=" + mark + '}';
	}
}
