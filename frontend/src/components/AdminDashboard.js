import React, { useState, useEffect } from "react";
import axios from "axios";

const AdminDashboard = () => {
  const [users, setUsers] = useState([]);
  const [logs, setLogs] = useState([]);
  const [passwordChangeRequests, setPasswordChangeRequests] = useState([]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [userId, setUserId] = useState("");
  const [storageLimit, setStorageLimit] = useState("");

  const adminId = localStorage.getItem("userId"); // Admin ID'si localStorage'dan alınır

  useEffect(() => {
    // Kullanıcıları ve logları yükle
    const fetchUsers = async () => {
      try {
        const response = await axios.get(`/admin/users/${adminId}`);
        setUsers(response.data);
      } catch (err) {
        setError("Unable to fetch users.");
      }
    };

    const fetchLogs = async () => {
      try {
        const response = await axios.get(`/admin/logs/${adminId}`);
        setLogs(response.data);
      } catch (err) {
        setError("Unable to fetch logs.");
      }
    };

    const fetchPasswordChangeRequests = async () => {
      try {
        const response = await axios.get(
          `/admin/password-change-requests/${adminId}`
        );
        setPasswordChangeRequests(response.data);
      } catch (err) {
        setError("Unable to fetch password change requests.");
      }
    };

    fetchUsers();
    fetchLogs();
    fetchPasswordChangeRequests();
  }, [adminId]);

  const handlePasswordChangeRequestApproval = async (requestId) => {
    try {
      await axios.post(
        `/admin/password-change-requests/${adminId}/${requestId}/approve`
      );
      alert("Password Change Request Approved");
    } catch (err) {
      setError("Failed to approve password change request");
    }
  };

  const handlePasswordChangeRequestRejection = async (requestId) => {
    try {
      await axios.post(
        `/admin/password-change-requests/${adminId}/${requestId}/reject`
      );
      alert("Password Change Request Rejected");
    } catch (err) {
      setError("Failed to reject password change request");
    }
  };

  const handleDeleteUser = async (userId) => {
    try {
      await axios.delete(`/admin/users/${adminId}/delete/${userId}`);
      alert("User Deleted Successfully");
      setUsers(users.filter((user) => user.id !== userId)); // Kullanıcıyı listeden sil
    } catch (err) {
      setError("Failed to delete user");
    }
  };

  const handleStorageLimitUpdate = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.put(
        `/admin/users/${adminId}/storage-limit/${userId}`,
        null, // Gövdeyi göndermiyoruz, çünkü storageLimit parametresi URL'de gönderilecek
        {
          params: {
            storageLimit: storageLimit,
          },
        }
      );
      setSuccess("Storage limit updated successfully!");
      setError("");
    } catch (err) {
      setSuccess("");
      setError("Failed to update storage limit");
    }
  };

  return (
    <div>
      <h2>Admin Dashboard</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}

      <h3>Users</h3>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Storage Limit</th>
            <th>Uploaded Files</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.username}</td>
              <td>{user.role}</td>
              <td>{user.storageLimit} GB</td>
              <td>
                {/* Eğer kullanıcı herhangi bir dosya yüklemişse, dosyaları listele */}
                {user.uploadedFiles && user.uploadedFiles.length > 0 ? (
                  <ul>
                    {user.uploadedFiles.map((file, index) => (
                      <li key={index}>{file}</li>
                    ))}
                  </ul>
                ) : (
                  <p>No files uploaded</p>
                )}
              </td>
              <td>
                <button onClick={() => handleDeleteUser(user.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h3>Logs</h3>
      <ul>
        {logs.map((log) => (
          <li key={log.id}>{log.message}</li>
        ))}
      </ul>

      <h3>Password Change Requests</h3>
      <ul>
        {passwordChangeRequests.map((request) => (
          <li key={request.id}>
            <span>{request.username}</span>
            <button
              onClick={() => handlePasswordChangeRequestApproval(request.id)}
            >
              Approve
            </button>
            <button
              onClick={() => handlePasswordChangeRequestRejection(request.id)}
            >
              Reject
            </button>
          </li>
        ))}
      </ul>

      <h2>Update User Storage Limit</h2>
      <form onSubmit={handleStorageLimitUpdate}>
        <div>
          <label>User ID:</label>
          <input
            type="text"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            required
          />
        </div>
        <div>
          <label>New Storage Limit:</label>
          <input
            type="number"
            value={storageLimit}
            onChange={(e) => setStorageLimit(e.target.value)}
            required
          />
        </div>
        <button type="submit">Update</button>
      </form>
    </div>
  );
};

export default AdminDashboard;
