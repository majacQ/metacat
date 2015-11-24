<%@ page language="java" %>
<!--
/**
  *  '$RCSfile$'
  *      Authors:     Matthew Perry
  *      Copyright:   2005 University of New Mexico and
  *                   Regents of the University of California and the
  *                   National Center for Ecological Analysis and Synthesis
  *      For Details: http://www.nceas.ucsb.edu/
  *
  *   '$Author$'
  *     '$Date$'
  * '$Revision$'
  * 
  * This program is free software; you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation; either version 2 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
  *
  */
-->
<!-- location options -->
<option value="-180,-90 180,90">Select a Location</option>
<option value="-91.956,47.87 -91.706,48.12">ACM Wilderness Field Station</option>
<option value="-74.36,43.86 -74.11,44.11">Adirondack Ecological Center</option>
<option value="-91.505,40.125 -91.255,40.375">Alice L. Kibbe Life Science Station</option>
<option value="-123.769444444,39.6041666667 -123.519444444,39.8541666667">Angelo Coast Range Reserve UCNRS</option>
<option value="-76.058,37.175 -75.808,37.425">Anheuser Busch Coastal Research Center</option>
<option value="-122.461111111,36.9833333333 -122.211111111,37.2333333333">Ano Nuevo Island Reserve UCNRS</option>
<option value="-110.631,31.466 -110.381,31.716">Appleton-Whittell Research Ranch</option>
<option value="-81.463,27.063 -81.213,27.313">Archbold Biological Station</option>
<option value="-61.375,15.375 -61.125,15.625">Archibold Tropical Research Center (Dominica)</option>
<option value="-149.725,68.475 -149.3254,68.725">Arctic LTER (ARC)</option>
<option value="-76.761,42.16 -76.511,42.41">Arnot Teaching and Research Forest</option>
<option value="-85.025,44.645 -84.775,44.895">Au Sable Institute of Environmental Studies</option>
<option value="-122.125,48.075 -121.875,48.325">Au-Sable Pacific Rim Facility</option>
<option value="-85.805,40.195 -85.555,40.445">Ball State Field Station and Evironmental Ed Center</option>
<option value="-76.425,38.975 -76.175,39.225">Baltimore Ecosystem Study LTER (BES)</option>
<option value="-125.258,48.709 -125.008,48.959">Bamfield Marine Station BC</option>
<option value="-74.025,41.905 -73.775,42.155">Bard College Field Station</option>
<option value="-81.513,40.984 -81.263,41.234">Bath Field Station</option>
<option value="-64.825,32.242 -64.575,32.492">Bermuda Biological Station</option>
<option value="-117.834,33.985 -117.584,34.235">Bernard Biological Field Station (Claremont Colleges)</option>
<option value="-79.425,25.575 -79.175,25.825">Bimini Biological Field Station</option>
<option value="-74.154,41.281 -73.904,41.531">Black Rock Forest</option>
<option value="-76.625,36.625 -76.375,36.875">Blackwater Ecological Preserve</option>
<option value="-122.93,48.429 -122.68,48.679">Blakely Island Field Station</option>
<option value="-78.19,38.939 -77.94,39.189">Blandy Experimental Farm</option>
<option value="-120.725,38.775 -120.475,39.025">Blodgett Forest Research Station</option>
<option value="-123.195,38.193 -122.945,38.443">Bodega Bay Marine Laboratory and Reserve</option>
<option value="-123.19,38.1819444444 -122.94,38.4319444444">Bodega Marine Reserve UCNRS</option>
<option value="-148.125,64.675 -147.875,64.925">Bonanza Creek LTER (BNZ)</option>
<option value="-66.959,44.376 -66.709,44.626">Bowdoin Scientific Station</option>
<option value="-117.420277778,33.8669444444 -117.170277778,34.1169444444">Box Springs Reserve UCNRS</option>
<option value="-116.509,33.597 -116.259,33.847">Boyd Deep Canyon Desert Research Center</option>
<option value="-97.857,30.149 -97.607,30.399">Brackenridge Field Laboratory</option>
<option value="-82.799,28.012 -82.549,28.262">Brooker Creek Preserve</option>
<option value="-93.201,36.441 -92.951,36.691">Bull Shoals Field Station</option>
<option value="-116.577777778,34.0138888889 -116.327777778,34.2638888889">Burns Pinon Ridge Reserve UCNRS</option>
<option value="-83.625,10.375 -83.375,10.625">Cano Palma Field Station</option>
<option value="-119.65,34.275 -119.4,34.525">Carpinteria Salt Marsh Reserve UCNRS</option>
<option value="-124.05,44.917 -123.8,45.167">Cascade Head Experimental Forest and Scenic Research Area</option>
<option value="-105.225,38.792 -104.975,39.042">Catamount Environmental Institute</option>
<option value="-93.325,45.275 -93.075,45.525">Cedar Creek LTER (CDR)</option>
<option value="-113.225,37.47 -112.975,37.72">Cedar Mountain Science Center</option>
<option value="-101.771,41.084 -101.521,41.334">Cedar Point Biological Station</option>
<option value="-112.195,33.415 -111.945,33.665">Central Arizona - Phoenix LTER (CAP)</option>
<option value="-120.4503,39.12175 -120.2003,39.37175">Chickering American River Reserve UCNRS</option>
<option value="-93.945,58.605 -93.695,58.855">Churchill Northern Studies Centre  Manitoba</option>
<option value="-116.277,48.025 -116.027,48.275">Clark Fork Field Station</option>
<option value="-92.647,46.58 -92.397,46.83">Cloquet Forestry Center</option>
<option value="-85.665,45.569 -85.415,45.819">CMU Biological Station</option>
<option value="-120.0,34.292 -119.75,34.542">Coal Oil Point Reserve</option>
<option value="-79.625,38.625 -79.375,38.875">Columbia-Greene Community College FS</option>
<option value="-93.004,41.588 -92.754,41.838">Conard (Henry S.)  Environmental Research Area</option>
<option value="-76.093,43.045 -75.843,43.295">Cornell Biological Field Station</option>
<option value="-83.625,34.875 -83.375,35.125">Coweeta Hydrologic Lab LTER (CWT)</option>
<option value="-74.962,44.097 -74.712,44.347">Cranberry Lake Biological Station</option>
<option value="-73.78,43.432 -73.53,43.682">Darrin Fresh Water Institute (Rensselaer)</option>
<option value="-117.380555556,33.0166666667 -117.130555556,33.2666666667">Dawson Los Monos Canyon Reserve UCNRS</option>
<option value="-98.507,50.057 -98.257,50.307">Delta Marsh Field Station Manitoba</option>
<option value="-119.938,39.405 -119.688,39.655">Desert Research Institute</option>
<option value="-111.284,32.109 -111.034,32.359">Desert Station</option>
<option value="-116.48,35.464 -116.23,35.714">Desert Studies Center</option>
<option value="-81.53,28.007 -81.28,28.257">Disney Wilderness Preserve</option>
<option value="-74.263,42.391 -74.013,42.641">E.N. Huyck Preserve and Biological Research</option>
<option value="-120.851944444,40.4922222222 -120.601944444,40.7422222222">Eagle Lake Field Station UCNRS</option>
<option value="-90.876,40.943 -90.626,41.193">Ecological Field Station-Monmoth College</option>
<option value="-85.325,39.758 -85.075,40.008">Ecology Research Center</option>
<option value="-84.848,39.407 -84.598,39.657">Ecology Research Center Miami University</option>
<option value="-90.112,35.226 -89.862,35.476">EJ Meeman Biological Station</option>
<option value="-117.2675,32.7433333333 -117.0175,32.9933333333">Elliot Chaparral Reserve UCNRS</option>
<option value="-65.945,18.198 -65.695,18.448">El Verde Field Station</option>
<option value="-117.164444444,33.3416666667 -116.914444444,33.5916666667">Emerson Oaks Reserve UCNRS</option>
<option value="-96.455,38.365 -96.205,38.615">Emporia State University Natural Area</option>
<option value="-84.089,42.338 -83.839,42.588">E S George Reserve</option>
<option value="-82.948,41.529 -82.698,41.779">F.T. Stone Laboratory</option>
<option value="-79.808,38.908 -79.558,39.158">Fernow Experimental Forest</option>
<option value="-114.157,47.751 -113.907,48.001">Flathead Lake Biological Station</option>
<option value="-80.125,24.875 -79.875,25.125">Florida Coastal Everglades LTER (FCE)</option>
<option value="-90.175,40.228 -89.925,40.478">Forbes Biological Station</option>
<option value="-121.891666667,36.5416666667 -121.641666667,36.7916666667">Fort Ord Natural Reserve UCNRS</option>
<option value="-117.181,32.719 -116.931,32.969">Fortuna Mountain Research Reserve</option>
<option value="-90.875,0.795 -90.625,1.045">Galapagos Academic Institute for Arts Sciences</option>
<option value="-73.258,45.392 -73.008,45.642">Gault Nature Reserve QC</option>
<option value="-81.425,31.275 -81.175,31.525">Georgia Coastal Ecosystems LTER (GCE)</option>
<option value="-74.592,23.992 -74.342,24.242">Gerace Research Center (San Salvador Isl. Bahamas)</option>
<option value="-84.125,39.535 -83.875,39.785">Glen Helen Ecology Institute</option>
<option value="14.921,-23.694 15.171,-23.444">Gobabeb Training and Research Centre</option>
<option value="-88.325,38.792 -88.075,39.042">Grasslands Wildlife Research Lab</option>
<option value="-90.147,38.808 -89.897,39.058">Great Rivers Field Station</option>
<option value="-89.454,41.589 -89.204,41.839">Green Wing Environmental Laboratory</option>
<option value="-122.325,44.075 -122.075,44.325">H.J. Andrews LTER (AND)</option>
<option value="-88.25,36.617 -88.0,36.867">Hancock Biological Station</option>
<option value="-123.881944444,39.1583333333 -123.631944444,39.4083333333">Hans Jenny Pygmy Forest Reserve UCNRS</option>
<option value="-72.325,42.375 -72.075,42.625">Harvard Forest LTER (HFR)</option>
<option value="-121.676,36.263 -121.426,36.513">Hastings Natural History Reservation</option>
<option value="-121.675,36.2630555556 -121.425,36.5130555556">Hastings Natural History Reservation UCNRS</option>
<option value="-124.177,44.512 -123.927,44.762">Hatfield Marine Science Center</option>
<option value="-76.105,40.521 -75.855,40.771">Hawk Mountain Sanctuary</option>
<option value="-83.313,34.929 -83.063,35.179">Highlands Biological Station</option>
<option value="-122.375,44.092 -122.125,44.342">HJ Andrews Experimental Forest</option>
<option value="-122.028,36.496 -121.778,36.746">Hopkins Marine Station</option>
<option value="-73.375,42.608 -73.125,42.858">Hopkins Memorial Forest</option>
<option value="-88.492,42.842 -88.242,43.092">Howard T. Greene Field Station</option>
<option value="-71.925,43.775 -71.675,44.025">Hubbard Brook LTER (HBR)</option>
<option value="-71.875,43.808 -71.625,44.058">Hubbard Brook Research Foundation</option>
<option value="-68.155,44.355 -67.905,44.605">Humboldt Field Research Institute</option>
<option value="-67.208,44.958 -66.958,45.208">Huntsman Marine Science Centre NB</option>
<option value="-87.795,46.725 -87.545,46.975">Huron Mt. Ives Lake Field Station</option>
<option value="-86.625,39.055 -86.375,39.305">Indiana Univ Research and Training Preserve</option>
<option value="-105.139,30.653 -104.889,30.903">Indio Mountains Research Station</option>
<option value="-77.325,37.192 -77.075,37.442">Inger and Walter Rice Center for Environmental Life Science</option>
<option value="-82.448,9.301 -82.198,9.551">Institute for Tropical Ecology and Conservation</option>
<option value="-61.525,15.192 -61.275,15.442">Institute for Tropical Marine Ecology</option>
<option value="-73.819,41.66 -73.569,41.91">Institute of  Ecosystem Studies</option>
<option value="-66.231111,18.343333 -65.981111,18.593333">Institute of Neurobiology</option>
<option value="-113.455,31.075 -113.205,31.325">Intercultural Center for Study of Deserts and Oceans</option>
<option value="-95.315,43.365 -95.065,43.615">Iowa Lakeside Laboratory</option>
<option value="-42.625,-21.125 -42.375,-20.875">Iracambi Atlantic Rainforest Research and Conservation Center</option>
<option value="-88.805,17.036 -88.555,17.286">Jaguar Creek</option>
<option value="-116.902777778,33.6833333333 -116.652777778,33.9333333333">James San Jacinto Mountains Reserve UCNRS</option>
<option value="-122.35,37.275 -122.1,37.525">Jasper Ridge Biological Preserve</option>
<option value="-123.503055556,38.3255555556 -123.253055556,38.5755555556">Jepson Prairie Reserve UCNRS</option>
<option value="-81.269,41.188 -81.019,41.438">JH Barrow Field Station</option>
<option value="-106.925,32.375 -106.675,32.625">Jornada LTER (JRN)</option>
<option value="-115.175,50.908 -114.925,51.158">Kananaskis Field Stations AB</option>
<option value="-95.325,38.908 -95.075,39.158">Kansas Ecological Reserves</option>
<option value="-88.875,39.375 -88.625,39.625">Kaskaskia Biological Station</option>
<option value="-93.125,44.008 -92.875,44.258">Katharine Ordway Natural History Study Area</option>
<option value="-82.107,29.571 -81.857,29.821">Katharine Ordway Preserve - Swisher Memorial Sanctuary</option>
<option value="-89.792,45.708 -89.542,45.958">Kemp Natural Resources Station</option>
<option value="-117.342,32.668 -117.092,32.918">Kendall Frost Marsh Reserve</option>
<option value="-117.341666667,32.6680555556 -117.091666667,32.9180555556">Kendall-Frost Mission Bay Marsh Reserve UCNRS</option>
<option value="-121.201416667,35.4031333333 -120.951416667,35.6531333333">Kenneth S. Norris Rancho Marino Reserve UCNRS</option>
<option value="-91.559,40.234 -91.309,40.484">Kibbe Life Sciences Field Station</option>
<option value="-112.15,24.9 -111.9,25.15">Kino Bay Center for Cultural and Ecological Studies (AZ)</option>
<option value="150.258,-35.675 150.508,-35.425">Kioloa Costal Campus - Edith and Joy London Foundation</option>
<option value="-79.625,43.885 -79.375,44.135">Koffler Scientific Reserve at Jokers Hill ON</option>
<option value="-96.475,38.925 -96.225,39.175">Konza Prairie Biological Station</option>
<option value="-94.725,38.975 -94.475,39.225">Konza Prairie LTER (KNZ)</option>
<option value="-75.454,41.246 -75.204,41.496">Lacawac Sanctuary</option>
<option value="-97.405,27.405 -97.155,27.655">Laguna Madre Field Station</option>
<option value="-83.524,41.564 -83.274,41.814">Lake Erie Center</option>
<option value="-95.317,47.103 -95.067,47.353">Lake Itasca Forestry and Biological Station</option>
<option value="-84.125,37.875 -83.875,38.125">Lake Louise Field Station</option>
<option value="-87.942,42.342 -87.692,42.592">Lake Michigan Biological Station</option>
<option value="-121.724,35.945 -121.474,36.195">Landels-Hill Big Creek Reserve</option>
<option value="-121.708333333,35.9416666667 -121.458333333,36.1916666667">Landels-Hill Big Creek Reserve UCNRS</option>
<option value="-89.108,16.608 -88.858,16.858">Las Cuevas Forest Research Station</option>
<option value="-84.132,10.306 -83.882,10.556">La Selva Biological Station</option>
<option value="-83.896,10.317 -83.646,10.567">La Suerte Biological Field Station</option>
<option value="-91.276,39.279 -91.026,39.529">Lay Field Station</option>
<option value="-83.145,36.965 -82.895,37.215">Lilley Cornett Woods</option>
<option value="-122.189,36.823 -121.939,37.073">Long Marine Laboratory</option>
<option value="-73.857,41.003 -73.607,41.253">Louis Calder Center - Biological Station</option>
<option value="-65.925,18.175 -65.675,18.425">Luquillo Experimental Forest LTER (LUQ)</option>
<option value="-81.275,27.015 -81.025,27.265">MacArthur Agro-Ecology Research Station</option>
<option value="-118.968,43.141 -118.718,43.391">Malheur Field Station</option>
<option value="-71.192,41.942 -70.942,42.192">Manomet Center for Conservation Sciences</option>
<option value="-72.566,40.755 -72.316,41.005">Marine Science Center</option>
<option value="-97.186,27.709 -96.936,27.959">Marine Science Institute</option>
<option value="-66.842,18.292 -66.592,18.542">Mata de Platano Field Station and Nature Reserve</option>
<option value="-116.226,44.786 -115.976,45.036">McCall Field Campus</option>
<option value="-122.556666667,38.7488888889 -122.306666667,38.9988888889">McLaughlin Natural Reserve UCNRS</option>
<option value="-122.848,39.334 -122.598,39.584">McLaughlin Reserve</option>
<option value="164.875,-78.125 165.125,-77.875">McMurdo Dry Valleys LTER (MCM)</option>
<option value="-80.145,38.575 -79.895,38.825">MeadWestvaco Wildlife and Ecosystem Res Forest</option>
<option value="-113.475,54.492 -113.225,54.742">Meanook Biological Research Station Alberta</option>
<option value="-111.875,35.042 -111.625,35.292">Merriam-Powel Center for Environmental Research</option>
<option value="-85.635,41.175 -85.385,41.425">Merry Lea Environmental Learning Center</option>
<option value="-74.225,41.625 -73.975,41.875">Mohonk Preserve Inc</option>
<option value="-121.913,36.675 -121.663,36.925">Moss Landing Marine Laboratory</option>
<option value="-117.383333333,33.6875 -117.133333333,33.9375">Motte Rimrock Reserve UCNRS</option>
<option value="-80.647,37.25 -80.397,37.5">Mountain Lake Biological Station</option>
<option value="-105.615,39.975 -105.365,40.225">Mountain Research Station</option>
<option value="-107.525,37.365 -107.275,37.615">Mountain Studies Institute</option>
<option value="-68.415,44.305 -68.165,44.555">Mount Desert Island Biological Laboratory</option>
<option value="-96.615,46.745 -96.365,46.995">MSUM Regional Sceince Center</option>
<option value="-70.171,41.264 -69.921,41.514">Nantucket Field Station</option>
<option value="-116.092,36.642 -115.842,36.892">Nevada Desert Ecological Research Facility</option>
<option value="-105.725,39.975 -105.475,40.225">Niwot Ridge/Green Lakes Valley LTER (NWT)</option>
<option value="-111.776,35.073 -111.526,35.323">Northern Arizona University Centennial Forest</option>
<option value="-89.825,45.875 -89.575,46.125">North Temperate Lakes LTER (NTL)</option>
<option value="-116.342,43.475 -116.092,43.725">Northwest Watershed Research Center</option>
<option value="-89.648,46.102 -89.398,46.352">Notre Dame Environmental Research Center</option>
<option value="-96.657,44.384 -96.407,44.634">Oak Lake Field Station</option>
<option value="-116.813266667,33.76675 -116.563266667,34.01675">Oasis de los Osos (James Reserve) UCNRS</option>
<option value="-124.454,43.215 -124.204,43.465">Oregon Institute of Marine Biology</option>
<option value="-94.134,34.324 -93.884,34.574">Ouachita Mountains Biological Station</option>
<option value="-64.125,-64.825 -63.875,-64.575">Palmer Station LTER (PAL)</option>
<option value="-122.858,37.808 -122.608,38.058">Palomarin Field Station (PRBO)</option>
<option value="-97.283,25.95 -97.033,26.2">Pan American Coastal Studies Laboratory</option>
<option value="-122.826,38.443 -122.576,38.693">Pepperwood Ranch Natural Preserve</option>
<option value="-122.487,37.04 -122.237,37.29">Pescadero Conservation Alliance</option>
<option value="-85.408,42.408 -85.158,42.658">Pierce Cedar Creek Institute</option>
<option value="-91.535,46.245 -91.285,46.495">Pigeon Lake Field Station</option>
<option value="-74.725,39.805 -74.475,40.055">Pinelands Field Research Station</option>
<option value="-82.799,28.012 -82.549,28.262">Pinellas County Biological Field Research Station at Broo</option>
<option value="-70.625,42.275 -70.375,42.525">Plum Island Ecosystem LTER (PIE)</option>
<option value="-79.364,40.072 -79.114,40.322">Powdermill Biological Station</option>
<option value="-73.694,44.715 -73.444,44.965">PSU-Ecosystem Studies Field Lab</option>
<option value="-73.608,44.142 -73.358,44.392">PSU-Twin Valleys Outdoor Education Center</option>
<option value="-73.567,44.485 -73.317,44.735">PSU-Valcour Kayaking and Docking Station</option>
<option value="-87.061,41.587 -86.811,41.837">Purdue Univ. N. Central Biology Field Station</option>
<option value="-80.549,41.531 -80.299,41.781">Pymatuning Laboratory of Ecology</option>
<option value="-122.366,38.693 -122.116,38.943">Quail Ridge Reserve</option>
<option value="-122.366111111,38.6927777778 -122.116111111,38.9427777778">Quail Ridge Reserve UCNRS</option>
<option value="-121.675,52.455 -121.425,52.705">Quesnel River Research Centre BC</option>
<option value="-83.936,9.428 -83.686,9.678">Quetzal Education Research Complex</option>
<option value="-78.275,40.233 -78.025,40.483">Raystown Field Station</option>
<option value="-111.908,40.673 -111.658,40.923">Red Butte Garden and Arboretum</option>
<option value="-89.475,36.259 -89.225,36.509">Reelfoot Lake Research and Teaching Center</option>
<option value="-91.355,37.855 -91.105,38.105">Reis Biological Station</option>
<option value="-76.636,43.33 -76.386,43.58">Rice Creek Field Station</option>
<option value="-148.958,-17.625 -148.708,-17.375">Richard B Gump South Pacific Biological Research Station</option>
<option value="-110.836,31.961 -110.586,32.211">Rincon Institute</option>
<option value="-81.222,27.253 -80.972,27.503">Riverwoods Field Laboratory</option>
<option value="-75.035,42.585 -74.785,42.835">Robert R. Smith Envir. Field Station (Oneonta)</option>
<option value="-83.509,37.428 -83.259,37.678">Robinson Forest</option>
<option value="-107.114,38.834 -106.864,39.084">Rocky Mountain Biological Laboratory</option>
<option value="-114.855833333,34.6725 -114.605833333,34.9225">Sacramento Mountains UCNRS</option>
<option value="-120.371,39.317 -120.121,39.567">Sagehen Creek Field Station</option>
<option value="-82.158,26.317 -81.908,26.567">Sanibel-Captiva Consv. Fndt. Marine Lab</option>
<option value="-117.983333333,33.5333333333 -117.733333333,33.7833333333">San Joaquin Freshwater Marsh Reserve UCNRS</option>
<option value="-119.125,33.875 -118.875,34.125">Santa Barbara Coastal LTER (SBC)</option>
<option value="-119.858333333,33.875 -119.608333333,34.125">Santa Cruz Island Reserve UCNRS</option>
<option value="-117.292,34.543 -117.042,34.793">Santa Margarita Biological Field Station</option>
<option value="-81.804,33.239 -81.554,33.489">Savannah River Enviormental Field Station</option>
<option value="-71.658,21.358 -71.408,21.608">School for Field Studies-Center for Marine Studies</option>
<option value="-117.379,32.75 -117.129,33.0">Scripps Coastal Reserve</option>
<option value="-117.379166667,32.75 -117.129166667,33.0">Scripps Coastal Reserve UCNRS</option>
<option value="-83.144,28.961 -82.894,29.211">Seahorse Key Marine Laboratory</option>
<option value="-72.852,40.689 -72.602,40.939">Seatuck MWR</option>
<option value="-120.239,34.543 -119.989,34.793">Sedgwick Reserve</option>
<option value="-120.134166667,34.5475 -119.884166667,34.7975">Sedgwick Reserve UCNRS</option>
<option value="-99.392,48.075 -99.142,48.325">Selman Living Laboratory</option>
<option value="-107.009,34.225 -106.759,34.475">Sevilleta Field Station</option>
<option value="-106.925,34.175 -106.675,34.425">Sevilleta LTER (SEV)</option>
<option value="-122.736,48.388 -122.486,48.638">Shannon Point Marine Center</option>
<option value="-70.74,42.864 -70.49,43.114">Shoals Marine Laboratory</option>
<option value="-104.925,40.675 -104.675,40.925">Shortgrass Steppe LTER (SGS)</option>
<option value="-104.905,40.585 -104.655,40.835">Shortgrass Steppe LTER Site</option>
<option value="-111.092,33.558 -110.842,33.808">Sierra Ancha Station</option>
<option value="-118.955,37.489 -118.705,37.739">Sierra Nevada Aquatic Research Lab</option>
<option value="-120.554,39.463 -120.304,39.713">Sierra Nevada Field Campus</option>
<option value="-116.748,33.252 -116.498,33.502">Sky Oaks Field Station</option>
<option value="-82.448,9.301 -82.198,9.551">Smithsonian Tropical Research Institute</option>
<option value="-111.367,32.004 -111.117,32.254">Sonoran Arthropod Studies Institute</option>
<option value="-109.333,31.759 -109.083,32.009">Southwestern Research Station</option>
<option value="-79.616,38.546 -79.366,38.796">Spruce Knob Mountain Center</option>
<option value="-81.275,31.542 -81.025,31.792">St. Catherines Island Research Station</option>
<option value="-117.678,33.504 -117.428,33.754">Starr Ranch Sanctuary</option>
<option value="-92.886,45.046 -92.636,45.296">St Croix Watershed Research Station</option>
<option value="-122.299,38.731 -122.049,38.981">Stebbins Cold Canyon Reserve</option>
<option value="-122.299444444,38.7313888889 -122.049444444,38.9813888889">Stebbins Cold Canyon Reserve UCNRS</option>
<option value="-97.298,29.956 -97.048,30.206">Stengl Lost Pines Biological Station</option>
<option value="-75.913,39.139 -75.663,39.389">Stroud Water Research Center</option>
<option value="-118.775,33.975 -118.525,34.225">Stunt Ranch Santa Monica Mountains Reserve</option>
<option value="-118.775,33.975 -118.525,34.225">Stunt Ranch Santa Monica Mountains Reserve UCNRS</option>
<option value="-74.955,42.625 -74.705,42.875">SUNY Oneonta Biological Field Station</option>
<option value="-115.789,34.681 -115.539,34.931">Sweeney Granite Mountains Desert Research Center</option>
<option value="-115.788888889,34.6805555556 -115.538888889,34.9305555556">Sweeney Granite Mountains Desert Reserve Center UCNRS</option>
<option value="-95.455,50.905 -95.205,51.155">Taiga Biological Station Manitoba</option>
<option value="-84.125,30.375 -83.875,30.625">Tall Timbers Research Station</option>
<option value="-114.979,44.979 -114.729,45.229">Taylor Ranch Wilderness Field Station</option>
<option value="-119.155,36.835 -118.905,37.085">Teakettle Experimental Forest</option>
<option value="-84.455,38.895 -84.205,39.145">Thomas More College</option>
<option value="-84.455,38.895 -84.205,39.145">Tifft Nature Preserve</option>
<option value="-117.239,32.429 -116.989,32.679">Tijuana River National Estuarine Research R</option>
<option value="-149.842,68.508 -149.592,68.758">Toolik Field Station</option>
<option value="-89.685,45.425 -89.435,45.675">Treehaven Field Station</option>
<option value="-72.294,41.708 -72.044,41.958">Trinity College Field Station</option>
<option value="-89.793,45.893 -89.543,46.143">Trout Lake Station</option>
<option value="-90.429,30.169 -90.179,30.419">Turtle Cove Environmental Research Station</option>
<option value="-90.684,38.412 -90.434,38.662">Tyson Research Center</option>
<option value="-90.425,36.755 -90.175,37.005">University Forest (Missouri)</option>
<option value="-84.822,45.456 -84.572,45.706">Univ of Michigan Biological Station Douglas L</option>
<option value="-89.517,34.3 -89.267,34.55">Univ of Mississippi Field Station</option>
<option value="-96.926,33.757 -96.676,34.007">Univ of Oklahoma Biological Station</option>
<option value="-88.146,43.257 -87.896,43.507">Univ of Wisconsin Milwaukee Field Station</option>
<option value="-99.24,26.389 -98.99,26.639">Upper Rio Grande Biological Station</option>
<option value="-118.954722222,37.4891666667 -118.704722222,37.7391666667">Valentine Eastern Sierra (SNARL) UCNRS</option>
<option value="-119.116666667,37.5 -118.866666667,37.75">Valentine Eastern Sierra (Valentine Camp) UCNRS</option>
<option value="-119.117,37.5 -118.867,37.75">Valentine Eastern Sierra Reserve</option>
<option value="-91.993,47.776 -91.743,48.026">Vermilion Sea Field Station</option>
<option value="-74.925,37.375 -74.675,37.625">Virginia Coast Reserve LTER (VCR)</option>
<option value="-85.536,42.275 -85.286,42.525">W.K. Kellogg Biological Station</option>
<option value="-85.525,42.275 -85.275,42.525">W.K. Kellogg Biological Station LTER (KBS)</option>
<option value="-112.969,34.798 -112.719,35.048">Walnut Creek Center for Education and Research</option>
<option value="-103.345,43.965 -103.095,44.215">Wheaton College Science Station</option>
<option value="-118.451,37.236 -118.201,37.486">White Mountain Research Station</option>
<option value="-79.625,38.625 -79.375,38.875">Whittaker Forest</option>
<option value="-120.003,39.15 -119.753,39.4">Whittell Forest and Wildlife Area</option>
<option value="-122.077,45.696 -121.827,45.946">Wind River Canopy Crane Research Facility</option>
<option value="-81.645,41.105 -81.395,41.355">Woodlake Environmental Field Station</option>
<option value="-118.625,33.308 -118.375,33.558">Wrigley Marine Science Center</option>
<option value="-110.714,43.732 -110.464,43.982">Wyoming National Park Service</option>
<option value="-109.958,44.875 -109.708,45.125">Yellowstone Ecosystem Studies Field Station</option>
<option value="-122.190833333,36.8258333333 -121.940833333,37.0758333333"/>Younger Lagoon Reserve UCNRS</option>