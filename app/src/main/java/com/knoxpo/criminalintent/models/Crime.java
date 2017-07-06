package com.knoxpo.criminalintent.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by knoxpo on 24/11/16.
 */

public class Crime {

    private static final String
            JSON_S_ID = "id",
            JSON_S_TITLE = "title",
            JSON_N_DATE = "date",
            JSON_B_IS_SOLVED = "is_solved",
            JSON_S_PERSON_INVOLVED = "person_involved";

    private static final long
            INVALID_DATE = -1;

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mIsSolved;
    private String mPersonInvolved;
    private String mPhotoFile;

    public Crime() {
        mId = UUID.randomUUID();
    }

    public Crime(JSONObject object) throws JSONException {
        mId = UUID.fromString(object.getString(JSON_S_ID));
        mTitle = object.getString(JSON_S_TITLE);

        long dateMillis = object.getLong(JSON_N_DATE);
        if(dateMillis == INVALID_DATE){
            mDate = null;
        }else{
            mDate = new Date(dateMillis);
        }
        mIsSolved = object.getBoolean(JSON_B_IS_SOLVED);
        mPersonInvolved = object.getString(JSON_S_PERSON_INVOLVED);
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mIsSolved;
    }

    public void setSolved(boolean solved) {
        mIsSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPersonInvolved(){
        return mPersonInvolved;
    }
    public void setPersonInvolved(String name){
        mPersonInvolved = name;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put(JSON_S_ID, mId);
            object.put(JSON_S_TITLE, mTitle);
            if(mDate == null){
                object.put(JSON_N_DATE,INVALID_DATE);
            }else{
                object.put(JSON_N_DATE, mDate.getTime());
            }
            object.put(JSON_B_IS_SOLVED, mIsSolved);
            object.put(JSON_S_PERSON_INVOLVED,mPersonInvolved);
        } catch (JSONException e) {
            //do nothing
        }
        return object;
    }
    public String getPhotoFIleName(){
        return "IMG_"+mId.toString()+".jpg";
    }
}
