# pip install sparqlwrapper
# https://rdflib.github.io/sparqlwrapper/

from random import randrange
import sys
from SPARQLWrapper import SPARQLWrapper, JSON

endpoint_url = "https://query.wikidata.org/sparql"

query = """SELECT ?child ?childLabel (SAMPLE (?lat) AS ?lat) (SAMPLE (?lon) AS ?lon) (SAMPLE (?visitors) AS ?visitors) (SAMPLE (?itemdesc) AS ?desc) (SAMPLE (?image) AS ?image)
WHERE  
{
  VALUES ?country { wd:Q38 wd:Q237 wd:Q238}
  ?type (wdt:P279*) wd:Q570116.
  ?child (wdt:P31*) ?type;
         wdt:P17 ?country;
         schema:description ?itemdesc;
         p:P625 ?coordinate.
  FILTER(LANG(?itemdesc) = "it")
  OPTIONAL { ?child wdt:P1174 ?visitors }
  BIND(if(bound(?visitors), ?visitors, 0) AS ?visitors)
  OPTIONAL { ?child wdt:P18 ?image }
  
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

f = open("tourist_attractions.sql", "w" , encoding='utf-8')

duration = [60, 120, 90, 150, 30]

for result in results["results"]["bindings"]:
    insert_string = ''

    durationIndex = randrange(5)

    insert_string += "INSERT INTO tourist_attraction(name, wikidata_url, description, image_url, lat, lon, visits, visit_duration_minutes) VALUES ("
    insert_string += "'" + result['childLabel']['value'].replace("'", "''").title() + "',"
    insert_string += "'" + result['child']['value'] + "',"
    insert_string += "'" + result['desc']['value'].replace("'", "''") .capitalize()+ "',"

    if("image" in result):
      insert_string += "'" + result['image']['value'] + "',"
    else:
      insert_string += "null,"

    insert_string += "" + result['lat']['value'] + ","
    insert_string += "" + result['lon']['value'] + ","
    insert_string += "" + result['visitors']['value'] + ","
    insert_string += "" + str(duration[durationIndex]) + ") ON CONFLICT DO NOTHING;\n"

    f.write(insert_string)

f.close()