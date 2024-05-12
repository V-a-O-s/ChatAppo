import React from 'react';
import axios from 'axios';
import authHeader from '../api/authHeader'; // Ensure this path is correct
import { API_URL } from '../config';

function Logout() {
    // Function to confirm and execute logout
    const confirmLogout = async () => {
        // Ask user to confirm the logout action
        if (window.confirm('Are you sure you want to logout?')) {
            try {
                const headers = { Authorization: authHeader() }; // Ensure authHeader returns the correct token format
                await axios.post(`${API_URL}/auth/logout`, {}, { headers });
                // Alert the user of successful logout
                alert('You have been logged out successfully.');
                // Add any post-logout logic here, e.g., redirect to login page
                // window.location = '/login'; // Uncomment and modify according to your routing setup
            } catch (error) {
                console.error('Logout failed:', error);
                // Alert the user of the failure
                alert('Logout failed. Please try again.');
            }
        }
    };

    return (
        <div className='content'>
            <MainNavigation />
            <p>Are you sure you want to be logged out?</p>
            <button type="button" onClick={confirmLogout}>Confirm</button>
        </div>
    );
}

export default Logout;
