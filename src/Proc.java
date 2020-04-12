/**
 * Class Implement of process object
 */
class Proc {
    private String procLabel;
    private int vt;

    public Proc(String procLabel, int vt) {
        this.procLabel = procLabel;
        this.vt = vt;
    }

    protected String getLabel() {
        return this.procLabel;
    }

    protected int getVt() {
        return this.vt;
    }

}
