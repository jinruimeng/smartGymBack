package common.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyUIDataGridResult implements Serializable{

	/**
	 * 第一版：2018.12.25添加
	 */
	private static final long serialVersionUID = -253579694464232055L;
	private long total;
	private List<Object> rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<Object> getRows() {
		return rows;
	}
	public void setRows(List<Object> rows) {
		this.rows = rows;
	}
	
}
