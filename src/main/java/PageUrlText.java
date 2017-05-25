
public class PageUrlText implements Comparable<PageUrlText>{
    String url;
    String text;
    String previousUrl;
    int depth;

    public PageUrlText(String url, String text, String previousUrl, int depth) {
        this.url = url;
        this.text = text;
        this.previousUrl = previousUrl;
        this.depth = depth;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public int compareTo(PageUrlText o) {
         if (this.getPreviousUrl().compareTo(o.getPreviousUrl())!=0) {
             return this.getPreviousUrl().compareTo(o.getPreviousUrl());
         }
        return Integer.compare(this.getDepth(), o.getDepth());
    }
}
