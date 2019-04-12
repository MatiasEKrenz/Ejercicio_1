public enum Clasificacion {

    CRITICO ("Critico"),
    NORMAL ("Normal"),
    MENOR ("Menor");

    private String status;

    Clasificacion(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
