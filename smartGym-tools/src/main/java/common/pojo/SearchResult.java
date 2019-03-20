package common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
	/**
	 * 第一版：2018.12.25添加
	 */
	private static final long serialVersionUID = 8625692871102862725L;
	
	private long recordCount;
	private int totalPages;
	private List<SearchItem> itemList;
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
}
