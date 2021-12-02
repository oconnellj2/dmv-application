
select name, count(success)
from Appointment, Service
where Appointment.service_id = Service.service_id
and success = 1
and app_date > app_date - 30
group by name;
