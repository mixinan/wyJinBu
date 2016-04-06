package mixinan.entity;

public class Bt {
	private String name;
	private String uri;
	private String pinyin;
	
	public Bt() {
		super();
	}
	public Bt(String name, String uri, String pinyin) {
		super();
		this.name = name;
		this.uri = uri;
		this.pinyin = pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	@Override
	public String toString() {
		return "Bt [name=" + name + ", uri=" + uri + ", pinyin=" + pinyin + "]";
	}
	
	
}
