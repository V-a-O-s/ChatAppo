import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import ChatWindow from './chat';

describe('ChatWindow Component', () => {
  it('sends a message', () => {
    const { getByPlaceholderText, getByText } = render(<ChatWindow activeChat={1} />);
    fireEvent.change(getByPlaceholderText('Your Message'), { target: { value: 'Hello' } });
    fireEvent.keyPress(getByPlaceholderText('Your Message'), { key: 'Enter', code: 13 });
    expect(getByText('Sending...')).toBeInTheDocument();
  });

  it('scrolls to the bottom on new message', () => {
    const { getByText } = render(<ChatWindow activeChat={1} />);
    const lastMessage = getByText('Last message text');
    expect(lastMessage).toBeVisible();
  });
});
