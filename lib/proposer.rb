require 'drb/drb'
require 'bitarray'

class Proposer
  # Proposer class, mix into agent class.
  attr_accessor :current_iid, :uri, :id, :leader_id, :ballot, :promises_count, 
                :promises_bitvector, :status

  @phase_1_info = Struct.new(:pending_count, :ready_count, 
                                       :highest_open)

  @phase_2_info = Struct.new(:next_unused_iid, :open_count)

  @status = [:empty, :p1_pending, :p1_ready, :p2_pending, :p2_completed]

  def promises_bitvector
    @promises_bitvector.nil? ? BitVecort.new : @promises_bitvector;
  end

  def status
    @status.nil? ? 'empty' : @status;
  end

end
