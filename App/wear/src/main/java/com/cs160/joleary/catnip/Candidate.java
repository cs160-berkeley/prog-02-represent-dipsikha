package com.cs160.joleary.catnip;

/**
 * Created by dipsikhahalder on 3/5/16.
 */
public class Candidate {


    private String name;
    private String party;
    private String email;
    private String website;
    private String twitter;



    private String bio_id;
    private String term_end;




    public Candidate(String name,
                     String party,
                     String email,
                     String website,
                     String twitter,
                     String bio_id,
                     String term_end){
        this.name = name;
        this.party = party;
        this.email = email;
        this.website = website;
        this.twitter = twitter;
        this.bio_id = bio_id;
        this.term_end = term_end;

    }
    public String getTerm_end() {
        return term_end;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getWebsite() {
        return website;
    }

    public String getBio_id() {
        return bio_id;
    }

}
