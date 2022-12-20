package ao.vivalabs.iska_minhas_notas.scraping;

public  interface IskaWebScrapingCallback {
    void postExecute(boolean loggedIn, boolean hasError);
}