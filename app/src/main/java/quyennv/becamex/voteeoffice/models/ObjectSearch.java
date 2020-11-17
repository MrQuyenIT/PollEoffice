package quyennv.becamex.voteeoffice.models;

import com.google.gson.annotations.SerializedName;

public class ObjectSearch {
    @SerializedName("userName")
    public String userName;
    @SerializedName("accountID")
    public int accountID;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("department")
    public String department;
    @SerializedName("departmentCode")
    public String departmentCode;
    @SerializedName("email")
    public String email;


    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public String getFullName() {
        return userName;
    }

    public void setFullName(String userName) {
        this.userName = userName;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
