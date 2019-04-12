public enum Estado {

    ASIGNADO ("Asignado"),
    RESUELTO ("Resuelto");

    private String status;

    Estado(String status) {
        this.status = status;
    }

    public String getStatus() {
    return status;
    }

    public void setStatus(String status) {
    this.status = status;
    }

}
