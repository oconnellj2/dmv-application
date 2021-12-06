
select d.name, sum(fee)
from service s, department d, document d
where d.service_id = s.service_id 
and s.service_id = d.did
and d.expiration_date between to_date('01/2021', 'MM/YYYY') and to_date('02/2021', 'MM/YYYY')
group by d.name
order by sum(fee) desc;