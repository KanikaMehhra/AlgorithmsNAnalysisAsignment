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

    public String getLabel() {
        return this.procLabel;
    }

    public int getVt() {
        return this.vt;
    }

    public boolean searchByVt(int vt) {
        if (this.vt == vt) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean searchByProcLabel(String procLabel) {
        if (this.procLabel == procLabel) {
            return true;
        }
        else {
            return false;
        }
    }
}
