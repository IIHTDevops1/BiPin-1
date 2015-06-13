package ivye.bipin.pojo;


public class ListData<T> {
    public int typeId;
    public T data;

    public ListData(int type, T data) {
        super();
        this.typeId = type;
        this.data = data;
    }

    public String toString() {
        return "ListData{typeId:" + typeId + ";data:" + data + "}";
    }
}
