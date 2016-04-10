'use strict';

const exec = require('child-process-promise').exec,
  fs = require('mz/fs'),
  boom = require('boom'),
  //co = require('../common.js'),
  ra = require('./resultAssembler');

/* result = {
  status: [-256-256]
  stage: String - syntax|literals|vocabs
  output: String,
  filename: String - name of the temporary file
}
*/
module.exports = {
  validate: function(request, reply) {
    checksyntax(request) //.checkliterals().checkvocabs()
      .then((result) => { //Erfolgreicher oder nicht erfolgreicher Durchlauf der Stage (Statuscode 0 und != 0)
        request.log('Erfolg\n', result);
        if (result.status === 0)
          reply('The Dataset is fine. Congratulations!');
        else if (result.status === 1) {
          reply(ra.assembleResultMessage(result));
        } else
          throw result;
        return result;
      })
      .catch((result) => { //Fehler bei der Ausführung
        request.log(result);
        reply(boom.badImplementation());
        return result;
      })
      .then((result) => fs.unlink(result.filename))
      .catch((err) => request.log('Failed to delete the temp file', err));
  }
};

function checksyntax(request) {
  const timestamp = new Date();
  const filename = 'tempfile_rapper_' + timestamp.getTime() + '.dat';

  return fs.writeFile(filename, request.payload.toString())
    .then((err) => {
      if (err) throw err;

      let contentTypeParam;
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
          return Promise.reject('N3 is not supported');
          break;
        default:
          contentTypeParam = '-g';
      }

      return exec('rapper ' + contentTypeParam + ' -o ntriples ' + filename)
        .then((result) => {
          return {
            status: 0,
            stage: 'syntax',
            output: result.stdout,
            filename: filename
          };
        })
        .catch((error) => {
          return {
            status: error.code,
            stage: 'syntax',
            message: error.stderr.toString(),
            filename: filename
          };
        });
    });
}
/*
function checkliterals() {

}

function checkvocabs() {
  //TODO implement Vocabulary checking
}
*/
