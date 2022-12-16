package ao.vivalabs.iska_minhas_notas.scraping;

public  interface IskaWebScrapingCallback {
    void preExecute();
    void postExecute(boolean loggedin, boolean hasError);
}