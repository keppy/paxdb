Gem::Specification.new do |s|
		       s.name        = 'paxdb'
		       s.version     = '0.0.0'
		       s.date        = '2013-09-01'
		       s.summary     = 'Graph database on leveldb using paxos.'
		       s.description = 'Graph database on leveldb using paxos protocol ideas.'
		       s.authors     = ["Keppy"]
		       s.email	     = 'keppy.rb@gmail.com'
		       s.files	     = ["lib/paxdb.rb"]
		       s.homepage     = 
		         'http://graphsockets.com'
		       s.license     = 'MIT'
		       s.add_dependency('leveldb-ruby', '>=0.15')
end