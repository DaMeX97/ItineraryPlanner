"use string";

var https = require('https');

const SPARQLQueryDispatcher = require('./../helper/SPARQLQueryDispatcher');
const mapper = require('./../helper/mapper');

class search_service {
    static plan (req, res, next) {
        const coordinates = {
            lat: 44.337222,
            long: 12.263333
        };
    
        const distanceRadius = 100; // 100 km
    
        // 6371.01 is earth radius in KM
        const radius = distanceRadius / 6371.01;
    
        const minLat = coordinates.lat - radius;
        const maxLat = coordinates.lat + radius;
        const deltaLon = Math.asin(Math.sin(radius) / Math.cos(coordinates.lat));
        const minLon = coordinates.long - deltaLon;
        const maxLon = coordinates.long + deltaLon;
        /* 
              wd:Q35145263
              wd:Q57660343
              wd:Q24398318
              wd:Q40080
              wd:Q8502
              wd:Q133056
              wd:Q4989906
              wd:Q174782
              wd:Q56055115
        */
        const endpointUrl = 'https://query.wikidata.org/sparql';
        const sparqlQuery = `SELECT DISTINCT ?child ?childLabel WHERE {
            VALUES ?ent {
              wd:Q570116
            }
            ?type (wdt:P279*) ?ent.
            ?child (wdt:P31*) ?type;
              p:P625 ?coordinate.
            ?child wdt:P17 wd:Q38.
            ?coordinate ps:P625 ?coord;
              psv:P625 ?coordinate_node.
            ?coordinate_node wikibase:geoLongitude ?lon.
            hint:Prior hint:rangeSafe "true"^^xsd:boolean.
            ?coordinate_node wikibase:geoLatitude ?lat.
            hint:Prior hint:rangeSafe "true"^^xsd:boolean.
            
            FILTER(xsd:float(?lat) >= ${minLat} && xsd:float(?lat) <= ${maxLat} && xsd:float(?lon) >= ${minLon} && xsd:float(?lon) <= ${maxLon})
            
            SERVICE wikibase:label { bd:serviceParam wikibase:language "it". }
        }`;

        console.log(sparqlQuery);

        const queryDispatcher = new SPARQLQueryDispatcher(endpointUrl);
        queryDispatcher.query(sparqlQuery).then((data) => {
            const places = data.results.bindings.map((elem) => mapper.fromSPARQLToProp(elem));

            const place = req.params.place;
            return res.status(200).json({
                error: false,
                messaggio: "",
                response: {
                    input: {
                        'place': place
                    },
                    output: {
                        data: places
                    }
                }
            });
        });
    }
}

module.exports = search_service;