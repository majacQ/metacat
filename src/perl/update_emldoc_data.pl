#!/usr/bin/perl
#
#  '$RCSfile: transfer_emldocs_from_metacat1_to_metacat2.pl,v $'
#  Copyright: 2000 Regents of the University of California 
#
#   '$Author: sgarg $'
#     '$Date: 2004/04/09 23:12:46 $'
# '$Revision: 1.1 $' 
# 
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#
#
# This is a CGI application for inserting metadata documents into
# the Metacat database.  It utilizes the Metacat.pm module for most work.
# You can specify two metacats and a list of documents. This script will
# copy documents from one metacat to another.


use Metacat;

my $metacat1;
my $docid;
my $rev;
my $error = 0;
my $xmldoc;
my $xa;
my $response;

my $listname = "kruger_docids";

my $metacat1_url = "http://dataknp.sanparks.org/sanparks/metacat";
my $username = "uid=judithk,o=SANParks,dc=ecoinformatics,dc=org";
my $password = "xxxxxx";
#my $metacat1_url = "http://fred.msi.ucsb.edu/knb/metacat";
#my $username = "uid=leinfelder,o=NCEAS,dc=ecoinformatics,dc=org";
#my $password = "xxxxxx";

#used for inserting xml in the appropriate location
my $updatedcount = 0;
my $insertionindex;
#my $insertionmarker = "</abstract>";
my $insertionmarker = "</keywordSet>";
my $xmlsnippet = "";
$xmlsnippet .= "<keywordSet>";
#$xmlsnippet .= "<keywordThesaurus>location</keywordThesaurus>";
$xmlsnippet .= "<keyword>SANParks, South Africa</keyword>";
$xmlsnippet .= "<keyword>Kruger National Park, South Africa</keyword>";
$xmlsnippet .= "</keywordSet>";

$metacat1 = Metacat->new();

if ($metacat1) {
    $metacat1->set_options( metacatUrl => $metacat1_url);
} else {
    #die "failed during metacat creation\n";
    print "Failed during metacat1 creation.";
    $error = 1;
}

# Login to metacat
print "Connecting to metacat1..........\n";
my $response1 = $metacat1->login($username, $password);
if (! $response1) {
    print $metacat1->getMessage();
    print "Failed during login: metacat1.\n";
    $error = 2;
} else {
    print "Connected to metacat1\n";
}

if($error == 0){
    open(file,$listname) || die ("Couldn't open the file");
    while(<file>) {
	chomp();
	$docid = $_;

	print "Original docid: $docid\n";

	#get the document
	$xmldoc = $metacat1->read($docid);
	$xa = $xmldoc->content;

	#construct the next docid revision
	#three parts to the docid: scope.id.rev
	@docidparts = split(/\./, $docid);
	$rev = @docidparts[2];
	$rev = $rev + 1;
	print "next rev: $rev\n";
	@docidparts[2] = $rev;
	$docid = join('.', @docidparts);
	
	print "Revised docid: $docid\n";

	#check for the inserted data (maybe we did it already)
	$insertionindex = index($xa, $xmlsnippet);
	if ($insertionindex > 0) {
		print "Already added the xml snippet to this doc\n";
		next;
	}

	#insert the snippet
	$insertionindex = index($xa, $insertionmarker);
	if ($insertionindex > 0) {
		#we want it after the end of the marker
		$insertionindex += length($insertionmarker);

		#construct the new xml with inserted elements
		$xa = substr($xa, 0, $insertionindex) . $xmlsnippet . substr($xa, $insertionindex);

		print "\n----------\n";
		#print $xa;
		#print "\n----------\n";
		
		#send the updated document
		$response = $metacat1->update($docid, $xa);
		print $metacat1->getMessage();

		$updatedcount++;
	}
    }
print "Updated $updatedcount documents\n";
} else {
    print $error;
}
