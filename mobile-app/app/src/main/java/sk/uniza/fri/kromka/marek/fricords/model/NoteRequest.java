package sk.uniza.fri.kromka.marek.fricords.model;

public class NoteRequest {
    private String header;
    private String text;
    private int priority;
    private String source;
    private String target;
    private String dateOdkedy;
    private String dateDokedy;
    private String targetGroup;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDateOdkedy() {
        return dateOdkedy;
    }

    public void setDateOdkedy(String dateOdkedy) {
        this.dateOdkedy = dateOdkedy;
    }

    public String getDateDokedy() {
        return dateDokedy;
    }

    public void setDateDokedy(String dateDokedy) {
        this.dateDokedy = dateDokedy;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }
}
