
select dep.name, sum(fee)
from service s, department dep, document d
where d.service_id = s.service_id 
and s.service_id = d.did
and d.issue_date between to_date('01/2021', 'MM/YYYY') and to_date('02/2050', 'MM/YYYY')
group by d.name
order by sum(fee) desc;