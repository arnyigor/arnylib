package com.arny.arnylib.files;

public class MediaFileInfo {
	private String fileName,filePath,fileType;
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
		return "_ID = " + getID() + " fileName = " + getFileName() + " fileType = "  + getFileType() + " filePath = "+  getFilePath();
	}
}