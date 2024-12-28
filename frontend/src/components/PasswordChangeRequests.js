// frontend/src/components/PasswordChangeRequests.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../auth/AuthContext";

const PasswordChangeRequests = () => {
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchRequests = async () => {
      setError(null); // Hataları temizle
      try {
        const response = await axios.get(
          `http://localhost:8080/admin/password-change-requests/${user.id}`
        );
        setRequests(response.data);
      } catch (err) {
        setError("Failed to fetch password change requests.");
        console.error("Fetch Password Change Request Error:", err);
      }
    };
    if (user && user.role === "ROLE_ADMIN") {
      fetchRequests();
    }
  }, [user]);

  const handleApproveRequest = async (requestId) => {
    setError(null); // Hataları temizle
    try {
      await axios.post(
        `http://localhost:8080/admin/password-change-requests/${user.id}/${requestId}/approve`
      );
      const updatedRequest = requests.filter((req) => req.id !== requestId);
      setRequests(updatedRequest);
    } catch (err) {
      setError("Failed to approve request.");
      console.error("Approve Request Error:", err);
    }
  };

  const handleRejectRequest = async (requestId) => {
    setError(null); // Hataları temizle
    try {
      await axios.post(
        `http://localhost:8080/admin/password-change-requests/${user.id}/${requestId}/reject`
      );
      const updatedRequest = requests.filter((req) => req.id !== requestId);
      setRequests(updatedRequest);
    } catch (err) {
      setError("Failed to reject request.");
      console.error("Reject Request Error:", err);
    }
  };

  return (
    <div>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>User ID</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((request) => (
            <tr key={request.id}>
              <td>{request.id}</td>
              <td>{request.userId}</td>
              <td>{request.status}</td>
              <td>
                <button
                  onClick={() => handleApproveRequest(request.id)}
                  disabled={request.status !== "PENDING"}
                >
                  Approve
                </button>
                <button
                  onClick={() => handleRejectRequest(request.id)}
                  disabled={request.status !== "PENDING"}
                >
                  Reject
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PasswordChangeRequests;
