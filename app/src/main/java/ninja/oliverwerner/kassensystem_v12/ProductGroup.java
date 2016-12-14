package ninja.oliverwerner.kassensystem_v12;

/**
 * Created by Oliver on 14.12.2016.
 */

public class ProductGroup {

    private int pGroupId;
    private String pGroupName;

    public ProductGroup(int pGroupId, String pGroupName){
        this.pGroupId = pGroupId;
        this.pGroupName = pGroupName;
    }

    public int getpGroupId() {
        return this.pGroupId;
    }

    public void setpGroupId(int pGroupId) {
        this.pGroupId = pGroupId;
    }

    public String getpGroupName() {
        return this.pGroupName;
    }

    public void setpGroupName(String pGroupName) {
        this.pGroupName = pGroupName;
    }
}
