package pw.aaron1011.intentinterceptor;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.URLSpan;

import java.io.Serializable;

public class CapturedIntent implements Parcelable {

    public Intent intent;
    public ResolveInfo resolveInfo;
    public ActivityInfo activityInfo;
    public String resolvedType;
    public int callingPid;
    public int callingUid;

    public CapturedIntent(Intent intent, ResolveInfo resolveInfo, ActivityInfo activityInfo, String resolvedType, int callingPid, int callingUid) {
        this.intent = intent;
        this.resolveInfo = resolveInfo;
        this.activityInfo = activityInfo;
        this.resolvedType = resolvedType;
        this.callingPid = callingPid;
        this.callingUid = callingUid;
    }

    protected CapturedIntent(Parcel in) {
        intent = in.readParcelable(Intent.class.getClassLoader());
        resolveInfo = in.readParcelable(ResolveInfo.class.getClassLoader());
        activityInfo = in.readParcelable(ActivityInfo.class.getClassLoader());
        resolvedType = in.readString();
        callingPid = in.readInt();
        callingUid = in.readInt();
    }

    public static final Creator<CapturedIntent> CREATOR = new Creator<CapturedIntent>() {
        @Override
        public CapturedIntent createFromParcel(Parcel in) {
            return new CapturedIntent(in);
        }

        @Override
        public CapturedIntent[] newArray(int size) {
            return new CapturedIntent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeParcelable(this.intent, i);
        out.writeParcelable(this.resolveInfo, i);
        out.writeParcelable(this.activityInfo, i);
        out.writeString(this.resolvedType);
        out.writeInt(this.callingPid);
        out.writeInt(this.callingUid);
    }

    @Override
    public String toString() {
        return "CapturedIntent{" +
                "intent=" + intent +
                ", resolveInfo=" + resolveInfo +
                ", activityInfo=" + activityInfo +
                ", resolvedType='" + resolvedType + '\'' +
                ", callingPid=" + callingPid +
                ", callingUid=" + callingUid +
                '}';
    }
}
