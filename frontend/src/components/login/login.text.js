import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react';
import LoginRegister from './login';
import authService from '../../services/authService';

jest.mock('../../services/authService'); // Mock the authService

describe('LoginRegister Component', () => {
  it('shows error on failed login', async () => {
    authService.login.mockRejectedValue(new Error('Login failed'));
    const { getByText, getByPlaceholderText } = render(<LoginRegister />);
    
    fireEvent.change(getByPlaceholderText('Email'), { target: { value: 'test@test.com' } });
    fireEvent.change(getByPlaceholderText('Password'), { target: { value: 'password' } });
    fireEvent.click(getByText('Login'));

    await waitFor(() => expect(getByText('Login failed')).toBeInTheDocument());
  });

  it('switches to registration mode', () => {
    const { getByText, getByPlaceholderText } = render(<LoginRegister />);
    fireEvent.click(getByText('Register'));
    expect(getByPlaceholderText('Username')).toBeInTheDocument();
  });
});
