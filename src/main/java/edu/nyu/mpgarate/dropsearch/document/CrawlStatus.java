package edu.nyu.mpgarate.dropsearch.document;

/**
 * Created by mike on 5/5/15.
 */
public class CrawlStatus {
    private Integer pagesCrawled;
    private Boolean doneCrawling;

    public CrawlStatus(Integer pagesCrawled, Boolean doneCrawling){
        this.pagesCrawled = pagesCrawled;
        this.doneCrawling = doneCrawling;
    }

    public Integer getPagesCrawled(){
        return pagesCrawled;
    }

    public Boolean getDoneCrawling(){
        return doneCrawling;
    }

    public void setPagesCrawled(Integer pagesCrawled){
        this.pagesCrawled = pagesCrawled;
    }

    public void setDoneCrawling(Boolean doneCrawling){
        this.doneCrawling = doneCrawling;
    }
}
