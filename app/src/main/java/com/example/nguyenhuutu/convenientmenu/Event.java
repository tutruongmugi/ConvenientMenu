package com.example.nguyenhuutu.convenientmenu;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Event implements Comparable {
    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventContent='" + eventContent + '\'' +
                ", eventImageFiles=" + eventImageFiles +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", restAccount='" + restAccount + '\'' +
                ", datePublish=" + datePublish +
                '}';
    }

    /**
     * Properties
     */
    private String eventId;
    private String eventName;
    private String eventContent;
    private List<String> eventImageFiles;
    private Date beginDate;
    private Date endDate;
    private String restAccount;
    private Date datePublish;
    private Bitmap imageEvent;
    private String imageUrl;
    public static int compareProperty;
    public final static int DATE = 0;

    /**
     * Constructor methods
     */
    public Event(String _eventId, String _eventContent, List<String> _eventImageFiles, Date _beginDate, Date _endDate, String _restAccount, Date _datePublish,String _eventName) {
        this.eventId = _eventId;
        this.eventName = _eventName;
        this.eventContent = _eventContent;
        this.eventImageFiles = _eventImageFiles;
        this.beginDate = _beginDate;
        this.endDate = _endDate;
        this.restAccount = _restAccount;
        this.datePublish = _datePublish;
        this.eventName = _eventName;
    }

    /**
     * Getter methods
     */

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public List<String> getEventImageFiles() {
        return eventImageFiles;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public String getBeginDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(beginDate);
    }

    public String getEndDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(endDate);
    }

    public String getRestAccount() {
        return restAccount;
    }

    public Date getDatePublish() {
        return datePublish;
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
            return imageEvent;
        } else {
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
            if (this.getDatePublish().compareTo(eventCmp.getDatePublish()) > 1) {
                result = -1;
            } else {
                result = 1;
            }
        }

        return result;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
