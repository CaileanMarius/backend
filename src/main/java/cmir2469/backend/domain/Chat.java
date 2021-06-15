package cmir2469.backend.domain;

import java.time.LocalDateTime;

public class Chat {

    private Integer ID;
    private Integer senderID;
    private Integer receiverID;
    private String message;
    private LocalDateTime createdDate;

    public Chat(Integer ID, Integer senderID, Integer receiverID, String message, LocalDateTime createdDate) {
        this.ID = ID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.createdDate = createdDate;
    }

    public Chat() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getSenderID() {
        return senderID;
    }

    public void setSenderID(Integer senderID) {
        this.senderID = senderID;
    }

    public Integer getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Integer receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "ID=" + ID +
                ", senderID=" + senderID +
                ", receiverID=" + receiverID +
                ", message='" + message + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
