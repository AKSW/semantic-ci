'use strict';

const exec = require('child-process-promise').exec,
  fs = require('mz').fs,
  co = require('../common.js'),
  ra = require('./resultAssembler');

/* result = {
  status: [-256-256]
  stage: String - syntax|literals|vocabs
  output: String
}
*/

module.exports = {
  validate: function(request, reply) {
    checksyntax(request) //.checkliterals().checkvocabs()
      .then((result) => { //Erfolgreicher oder nicht erfolgreicher Durchlauf der Stage (Statuscode 0 und != 0)
        request.log(result);
        if (result.childProcess.exitCode === 0)
          reply('The Dataset is fine. Congratulations!');
        else {
          request.log('trying message');
          reply(ra.assembleResultMessage(result.stdout));
        }
      })
      .catch((result) => { //Fehler bei der Ausführung
        console.log(result);
        reply(ra.assembleResultMessage(result));
      });
  }
};

function checksyntax(request) {
  const timestamp = new Date();
  const filename = 'tempfile_rapper_' + timestamp.getTime() + '.dat';

  return fs.writeFile(filename, request.payload.toString())
    .then(() => {
      let contentTypeParam = '-g';
      switch (request.mime) {
        case 'application/rdf+xml':
          contentTypeParam = '-i rdfxml';
          break;
        case 'application/ld+json':
        case 'application/rdf+json':
          contentTypeParam = '-i json';
          break;
        case 'application/trig':
          contentTypeParam = '-i trig';
          break;
        case 'text/turtle':
          contentTypeParam = '-i turtle';
          break;
        case 'application/n-triples':
          contentTypeParam = '-i ntriples';
          break;
        case 'application/n-quads':
          contentTypeParam = '-i nquads';
          break;
        case 'text/n3':
          //not supported with rapper/raptor
          break;

        default:
          return Promise.reject('WrongMimeType');
      }

      return exec('rapper ' + contentTypeParam + ' -o ntriples ' + filename);
    });
    /*.catch((error) => {
      console.log('internal Error', error);
      return Promise.reject('FailedWritingFile');
    });*/
}

function checkliterals() {

}

function checkvocabs() {

}
