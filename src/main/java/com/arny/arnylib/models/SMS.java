package com.arny.arnylib.models;

public class SMS {
	public static final int STATUS_COMPLETE = 0;
	public static final int STATUS_FAILED = 64;
	public static final int STATUS_NONE = -1;
	public static final int STATUS_PENDING = 32;
	public static final int MESSAGE_TYPE_ALL = 0;
	public static final int MESSAGE_TYPE_DRAFT = 3;
	public static final int MESSAGE_TYPE_FAILED = 5;
	public static final int MESSAGE_TYPE_INBOX = 1;
	public static final int MESSAGE_TYPE_OUTBOX = 4;
	public static final int MESSAGE_TYPE_QUEUED = 6;
	public static final int MESSAGE_TYPE_SENT = 2;
	private String _id,address,body,service_center,subject;
	private String creator;//not changed by app
	private long date, date_sent;
	private int error_code,_count, person, status;
	private boolean read,locked,seen;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getService_center() {
		return service_center;
	}

	public void setService_center(String service_center) {
		this.service_center = service_center;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate_sent() {
		return date_sent;
	}

	public void setDate_sent(long date_sent) {
		this.date_sent = date_sent;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public int getPerson() {
		return person;
	}

	public void setPerson(int person) {
		this.person = person;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	@Override
	public String toString() {
		return "_id:"+_id+" address:" + address + " creator:" + creator + " person:" + person;
	}

	public int get_count() {
		return _count;
	}

	public void set_count(int _count) {
		this._count = _count;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
}
