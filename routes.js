'use strict';

const Joi = require('joi'),
  handlers = require('./controllers/handler');

module.exports = function(server) {
  server.route({
    method: 'POST',
    path: '/validate',
    handler: handlers.validate,
    config: {
      validate: {
        payload: Joi.any().required(),
      },
      payload: {
        parse: 'gunzip',
        allow: ['application/rdf+json', 'application/rdf+xml', 'application/trig', 'text/turtle', 'application/n-triples', 'application/n-quads', 'text/n3', 'application/ld+json']
      },
      tags: ['api'],
      description: 'Validate Semantic Data',
      plugins: {
        'hapi-swagger': {
          consumes: ['application/rdf+json', 'application/rdf+xml', 'application/trig', 'text/turtle', 'application/n-triples', 'application/n-quads', 'text/n3', 'application/ld+json']
        }
      },
    }
  });
};
