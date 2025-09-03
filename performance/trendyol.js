import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 100,
  duration: '1m',
};

export default function () {
  let res = http.get('https://www.trendyol.com/sr?q=shoes');
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 2000ms': (r) => r.timings.duration < 2000,
  });
  sleep(1);
}

