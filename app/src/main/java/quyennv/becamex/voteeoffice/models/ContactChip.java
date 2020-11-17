package quyennv.becamex.voteeoffice.models;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tylersuehr.chips.Chip;

public class ContactChip extends Chip implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("fullId")
    private int fullId;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private String code;
    @SerializedName("phone")
    private String phone;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("departmentName")
    private String departmentName;
    @SerializedName("departmentFullCode")
    private String departmentFullCode;



    private Uri avatarUri;

    private Drawable avatarDr;

    public ContactChip() {

    }

    public ContactChip(int _id, int _fullID, String _name, String _code, String _phone, String _email, String _avatar) {
        this.id = _id;
        this.fullId = _fullID;
        this.avatar = _avatar;
        this.name = _name;
        this.code = _code;
        this.phone = _phone;
        this.email = _email;
    }

    protected ContactChip(Parcel in) {
        id = in.readInt();
        fullId = in.readInt();
        name = in.readString();
        code = in.readString();
        phone = in.readString();
        email = in.readString();
        avatar = in.readString();
        avatarUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<ContactChip> CREATOR = new Creator<ContactChip>() {
        @Override
        public ContactChip createFromParcel(Parcel in) {
            return new ContactChip(in);
        }

        @Override
        public ContactChip[] newArray(int size) {
            return new ContactChip[size];
        }
    };

    @Nullable
    @Override
    public Object getId() {
        if(email.equals(""))
            return fullId;
        else
            return id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        if(email.equals(""))
            return null;
        else
            return departmentName;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        if(avatar.equals(""))
            return null;
        else
        {
            return Uri.parse( avatar );
        }
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(fullId);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeParcelable(avatarUri, flags);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFullId() {
        return fullId;
    }

    public void setFullId(int fullId) {
        this.fullId = fullId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAvatarUri(Uri avatarUri) {
        this.avatarUri = avatarUri;
    }

    public Drawable getAvatarDr() {
        return avatarDr;
    }

    public void setAvatarDr(Drawable avatarDr) {
        this.avatarDr = avatarDr;
    }
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentFullCode() {
        return departmentFullCode;
    }

    public void setDepartmentFullCode(String departmentFullCode) {
        this.departmentFullCode = departmentFullCode;
    }
}

