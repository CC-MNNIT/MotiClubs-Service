export interface User {
    uid: number;
    regno: string;
    name: string;
    email: string;
    course: string;
    phone: string;
    avatar: string;
};

export interface AdminUser extends User {
    cid: number;
};
