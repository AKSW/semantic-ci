'use strict';

const boom = require('boom'),
  exec = require('child-process-promise').exec,
  fs = require('mz').fs;

module.exports = {
  validate: function(request, reply) {
    checksyntax(request) //.checkliterals().checkvocabs()
      .then((result) => {
        reply({
          output: result.stdout,
          error: result.stderr
        });
      })
      .catch((error) => {
        console.log('Error', error);
        reply(boom.badImplementation());
      });
  }
};

function checksyntax(request) {
  const timestamp = new Date();
  const filename = 'tempfile_rapper_' + timestamp.getTime() + '.dat';

  return fs.writeFile(filename, request.payload.toString())
    .then((data) => {
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
        case 'application/turtle':
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
          return (new Promise()).reject('WrongMimeType');
      }

      return exec('rapper ' + contentTypeParam + ' -o ntriples ' + filename);
    })
    .catch((error) => {
      console.log('internal Error', error);
      return Promise.reject('FailedWritingFile');
    });
}

function checkliterals() {

}

function checkvocabs() {

}
