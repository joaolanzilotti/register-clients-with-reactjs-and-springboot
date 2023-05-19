import axios from 'axios';

const api = axios.create({
    baseURL: 'http://ec2-3-80-26-121.compute-1.amazonaws.com',
});
export default api;