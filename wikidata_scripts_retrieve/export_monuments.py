# pip install sparqlwrapper
# https://rdflib.github.io/sparqlwrapper/

import sys
from SPARQLWrapper import SPARQLWrapper, JSON

endpoint_url = "https://query.wikidata.org/sparql"

query = """SELECT ?child ?childLabel (SAMPLE (?lat) AS ?lat) (SAMPLE (?lon) AS ?lon)
WHERE 
{
  VALUES ?country { wd:Q38 wd:Q237 wd:Q238}
  ?child wdt:P31 wd:Q4989906;
         wdt:P17 ?country;
         p:P625 ?coordinate.
  
  ?coordinate ps:P625 ?coord;
              psv:P625 ?coordinate_node.
            ?coordinate_node wikibase:geoLongitude ?lon.
            ?coordinate_node wikibase:geoLatitude ?lat.
  SERVICE wikibase:label { bd:serviceParam wikibase:language "it". }
}
GROUP BY ?child ?childLabel"""


def get_results(endpoint_url, query):
    user_agent = "WDQS-example Python/%s.%s" % (sys.version_info[0], sys.version_info[1])
    # TODO adjust user agent; see https://w.wiki/CX6
    sparql = SPARQLWrapper(endpoint_url, agent=user_agent)
    sparql.setQuery(query)
    sparql.setReturnFormat(JSON)
    return sparql.query().convert()


results = get_results(endpoint_url, query)

f = open("monuments.sql", "w" , encoding='utf-8')

for result in results["results"]["bindings"]:
    insert_string = ''

    insert_string += "INSERT INTO place(name, wikidata_url, lat, lon) VALUES ("
    insert_string += "'" + result['childLabel']['value'].replace("'", "''") + "',"
    insert_string += "'" + result['child']['value'] + "',"
    insert_string += "" + result['lat']['value'] + ","
    insert_string += "" + result['lon']['value'] + ") ON CONFLICT DO NOTHING;\n"

    f.write(insert_string)

    update_type_string = ''

    update_type_string += "INSERT INTO monument(place_id) "
    update_type_string += " SELECT id "
    update_type_string += " FROM place p "
    update_type_string += " WHERE p.wikidata_url = '" + result['child']['value'] + "'"
    update_type_string += " ;\n"

    f.write(update_type_string)

f.close()