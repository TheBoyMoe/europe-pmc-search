package com.example.downloaderdemo.model;

// result of search query
/*

{
   "version":"4.4.0",
   "hitCount":4255,
   "request":{
      "query":"rrr",
      "resultType":"core",
      "synonym":false,
      "page":1,
      "pageSize":1
   },
   "resultList":{
      "result":[
         {
            "id":"26752278",
            "source":"MED",
            "pmid":"26752278",
            "title":"The impact of HIV infection on treatment outcome of tuberculosis: analysis of surveillance data from nine European countries, 2010-2012.",
            "authorString":"Karo B, Krause G, Hollo V, van der Werf MJ, Castell S, Hamouda O, Haas W.",
            "authorList":{
               "author":[  ]
            },
            "journalInfo":{
               "journalIssueId":2359420,
               "dateOfPublication":"2016 Jan",
               "monthOfPublication":1,
               "yearOfPublication":2016,
               "printPublicationDate":"2016-01-01",
               "journal":{
                  "title":"AIDS (London, England)",
                  "medlineAbbreviation":"AIDS",
                  "essn":"1473-5571",
                  "issn":"0269-9370",
                  "isoabbreviation":"AIDS",
                  "nlmid":"8710219"
               }
            },
            "abstractText":"abstract goes here ......."
            "affiliation":"aDepartment for Infectious Disease Epidemiology ...",
            "language":"eng",
            "pubModel":"Print-Electronic",
            "pubTypeList":{
               "pubType":[
                  "Journal Article"
               ]
            },
            "inEPMC":"N",
            "inPMC":"N",
            "hasPDF":"N",
            "hasBook":"N",
            "citedByCount":0,
            "hasReferences":"N",
            "hasTextMinedTerms":"N",
            "hasDbCrossReferences":"N",
            "hasLabsLinks":"N",
            "hasTMAccessionNumbers":"N",
            "dateOfCreation":"2016-01-11",
            "dateOfRevision":"2016-01-12",
            "electronicPublicationDate":"2016-01-08",
            "firstPublicationDate":"2016-01-08",
            "luceneScore":"NaN"
         }
      ]
   }
}


 */

public class ResultQuery {

    private ResultList resultList;

    public ResultList getResultList() {
        return resultList;
    }
}
