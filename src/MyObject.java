import java.util.Arrays;
import java.util.List;

public class MyObject {
	List<String> domains;
	String country;
	List<String> web_pages;
	String name;
	String alpha_two_code;
	String state;
	public MyObject(String name1,String country1,String alpha_two_code1,String state1, List<String> domains1 ,
			List<String> web_pages1) {
		this.name=name1;
		this.country=country1;
		this.alpha_two_code=alpha_two_code1;
		this.state=state1;
		this.domains=domains1;
		this.domains=domains1;
		this.web_pages=web_pages1;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public List<String> getDomains() {
		return domains;
	}

	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public List<String> getWeb_pages() {
		return web_pages;
	}

	public void setWeb_pages(List<String> web_pages) {
		this.web_pages = web_pages;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlpha_two_code() {
		return alpha_two_code;
	}
	public void setAlpha_two_code(String alpha_two_code) {
		this.alpha_two_code = alpha_two_code;
	}
}
