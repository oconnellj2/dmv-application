SELECT last_number
  FROM all_sequences
 WHERE sequence_owner = 'dryngler'
   AND sequence_name = 'customer_seq';

SELECT *
  FROM user_sequences
WHERE sequence_name = 'appointment_seq';