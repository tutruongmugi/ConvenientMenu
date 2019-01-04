package com.example.nguyenhuutu.convenientmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Event implements Comparable {
    public Event() {}

    private static final String TAG ="Event";

    public String getEvent_id() {
        return event_id;
    }

    public Event setEvent_id(String event_id) {
        this.event_id = event_id;
        return this;
    }

    public String getEvent_name() {
        return event_name;
    }

    public Event setEvent_name(String event_name) {
        this.event_name = event_name;
        return this;
    }

    public String getEvent_content() {
        return event_content;
    }

    public Event setEvent_content(String event_content) {
        this.event_content = event_content;
        return this;
    }

    public List<String> getEvent_image_files() {
        return event_image_files;
    }

    public Event setEvent_image_files(List<String> event_image_files) {
        this.event_image_files = event_image_files;
        return this;
    }

    public Date getBegin_date() {
        return begin_date;
    }

    public Event setBegin_date(Date begin_date) {
        this.begin_date = begin_date;
        return this;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public Event setEnd_date(Date end_date) {
        this.end_date = end_date;
        return this;
    }

    public String getRest_account() {
        return rest_account;
    }

    public Event setRest_account(String rest_account) {
        this.rest_account = rest_account;
        return this;
    }

    public Date getDate_publish() {
        return date_publish;
    }

    public Event setDate_publish(Date date_publish) {
        this.date_publish = date_publish;
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event_id='" + event_id + '\'' +
                ", event_name='" + event_name + '\'' +
                ", event_content='" + event_content + '\'' +
                ", event_image_files=" + event_image_files +
                ", begin_date=" + begin_date +
                ", end_date=" + end_date +
                ", rest_account='" + rest_account + '\'' +
                ", date_publish=" + date_publish +
                '}';
    }

    /**
     * Properties
     */

    @SerializedName("event_Id")
    private String event_id;
    @SerializedName("event_name")
    private String event_name;
    @SerializedName("event_content")
    private String event_content;
    @SerializedName("event_image_files")
    private List<String> event_image_files;
    @SerializedName("begin_date")
    private Date begin_date;
    @SerializedName("end_date")
    private Date end_date;
    @SerializedName("rest_account")
    private String rest_account;
    @SerializedName("date_publish")
    private Date date_publish;


    private Bitmap imageEvent =null;
    public static int compareProperty;
    public final static int DATE = 0;

    /**
     * Constructor methods
     */
    public Event(String _eventId, String _eventContent, List<String> _eventImageFiles, Date _beginDate, Date _endDate, String _restAccount, Date _datePublish,String _eventName) {
        this.event_id = _eventId;
        this.event_name = _eventName;
        this.event_content = _eventContent;
        this.event_image_files = _eventImageFiles;
        this.begin_date = _beginDate;
        this.end_date = _endDate;
        this.rest_account = _restAccount;
        this.date_publish = _datePublish;
        this.event_name = _eventName;
    }



    public Bitmap getImageEvent() {
        return imageEvent;
    }

    public void setImageEvent(Bitmap imageEvent) {
        try {
            this.imageEvent = imageEvent;
        } catch (Exception ex) {
        }
    }

    public Bitmap getImageEvent(Context context) {
        if (imageEvent != null) {
            Log.d(TAG, "getImageEvent: image is available");
            return imageEvent;
        } else {
            Log.d(TAG, "getImageEvent: image is null");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        }
    }
    /**
     * Other methods
     */

    /**
     * createEventId()
     * - Create new id for event
     *
     * @param idNum
     * @return String
     */
    public String createEventId(Integer idNum) {
        String newId;

        if (idNum < 10) {
            newId = String.format("EVENT000%d", idNum);
        } else if (idNum < 100) {
            newId = String.format("EVENT00%d", idNum);
        } else if (idNum < 1000) {
            newId = String.format("EVENT0%d", idNum);
        } else {
            newId = String.format("EVENT%d", idNum);
        }

        return newId;
    }

    /**
     * loadEvent()
     * - Load Event data from document
     *
     * @param document
     * @return Event
     */
    public static Event loadEvent(Map<String, Object> document) {
        String _eventId = document.get("event_id").toString();
        String _eventContent = document.get("event_content").toString();
        List<String> _eventImageFiles = (ArrayList) document.get("event_image_files");
        Date _beginDate = (Date) document.get("begin_date");
        Date _endDate = (Date) document.get("end_date");
        String _restAccount = document.get("rest_account").toString();
        Date _datePublish = (Date) document.get("date_publish");
        String _eventName = document.get("event_name").toString();

        return new Event(_eventId, _eventContent, _eventImageFiles, _beginDate, _endDate, _restAccount, _datePublish,_eventName);
    }

    /**
     * Create event's data for insert to database
     *
     * @param _eventId
     * @param _eventContent
     * @param _eventImageFiles
     * @param _beginDate
     * @param _endDate
     * @param _restAccount
     * @return
     */
    public static Map<String, Object> createEventData(String _eventId, String _eventName, String _eventContent, List<String> _eventImageFiles, Date _beginDate, Date _endDate, String _restAccount) {
        Map<String, Object> document = new HashMap<>();

        document.put("event_id", _eventId);
        document.put("event_name", _eventName);
        document.put("event_content", _eventContent);
        document.put("event_image_files", _eventImageFiles);
        document.put("begin_date", new Timestamp(_beginDate.getTime()));
        document.put("end_date", new Timestamp(_endDate.getTime()));
        document.put("rest_account", _restAccount);
        document.put("date_publish", new Timestamp((new Date()).getTime()));

        // thiếu upload ảnh

        return document;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Event eventCmp = (Event) o;
        int result = 0;

        if (compareProperty == DATE) {
            if (this.getDate_publish().compareTo(eventCmp.getDate_publish()) > 1) {
                result = -1;
            } else {
                result = 1;
            }
        }

        return result;
    }
}
