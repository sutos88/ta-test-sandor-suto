/**
 * It is used for collect data from webelements.
 */
class SiteInfos implements Comparable<SiteInfos>{
    String url;
    String text;
    String previousUrl;
    int depth;
    /**
     * @param url the website's url
     * @param text the website's text
     * @param previousUrl this is the previous site's url, from where we came from.
     * @param depth this is the actual depth number
     */
    public SiteInfos(String url, String text, String previousUrl, int depth) {
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

    public void setText(String text) {
        this.text = text;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public int compareTo(SiteInfos o) {
         if (this.getUrl().compareTo(o.getPreviousUrl())!=0) {
             return this.getUrl().compareTo(o.getPreviousUrl());
         }
        return this.getDepth()-o.getDepth();
    }

    @Override
    public String toString() {
        String result = "\t";
        for (int i = 0; i < depth; i++) {
            result.concat("\t");
        }
        return result + text + " " + url + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteInfos that = (SiteInfos) o;

        if (depth != that.depth) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return previousUrl != null ? previousUrl.equals(that.previousUrl) : that.previousUrl == null;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (previousUrl != null ? previousUrl.hashCode() : 0);
        result = 31 * result + depth;
        return result;
    }
}
