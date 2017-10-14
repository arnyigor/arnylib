package com.arny.arnylib.files;

import com.arny.arnylib.utils.Utility;
import org.chalup.microorm.annotations.Column;
public class MediaFileInfo {
	@Column("name")
	private String fileName;
	@Column("path")
	private String filePath;
	private String fileType;
	private int ID;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	@Override
	public String toString() {
		return Utility.getFields(this);
	}
}